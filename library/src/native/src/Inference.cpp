//
// Created by Ayodele Kehinde on 21/01/2025.
//

#include "Inference.h"

#include "gguf.h"
#include "inferkt.h"
#include <iostream>
#include "llama-impl.h"
#include "llama-kv-cache.h"
#include "llama-model.h"
#include <thread>
#include <unistd.h>

#include "common.h"

Inference::Inference() {
    llama_backend_init();
}

Inference::~Inference() {
    clean_up();
}

bool Inference::load_model(const model_settings &settings) {
    if (model){
        reset();
    }
    llama_model_params model_params = llama_model_default_params();
    model_params.n_gpu_layers = settings.number_of_gpu_layers;
    model_params.use_mmap = settings.use_mmap;
    model_params.use_mlock = settings.use_mlock;
    model_params.progress_callback = settings.callback;
    model_params.progress_callback_user_data = settings.progress_callback_user_data;
    model = llama_model_load_from_file(settings.model_path, model_params);
    if (!model) {
        clean_up();
        return false;
    }
    return true;
}

bool Inference::init_context(int context_length, int n_batch, int number_of_threads) {
    llama_context_params ctx_param = llama_context_default_params();
    ctx_param.n_ctx = context_length;
    ctx_param.n_batch = n_batch;
    ctx_param.n_ubatch = n_batch;
    ctx_param.n_threads = number_of_threads;
    ctx_param.n_threads_batch = number_of_threads;

    // Create context
    ctx = llama_init_from_model(model, ctx_param);
    vocab = llama_model_get_vocab(model);
    formatted.resize(llama_n_ctx(ctx));

    return ctx != nullptr && vocab != nullptr;
}

model_details Inference::get_model_details(const model_settings &settings) {
    model_details details{};
    const gguf_init_params params = {
        false,
        nullptr,
   };
    gguf_context * ctx = gguf_init_from_file(settings.model_path, params);
    details.version = gguf_get_version(ctx);

    const int n_kv = gguf_get_n_kv(ctx);

    for (int i = 0; i < n_kv; ++i) {
        const char * key = gguf_get_key(ctx, i);
        if (strcmp(key, "general.architecture") == 0) {
            details.architecture = gguf_kv_to_str(ctx, i).c_str();
        }
        if (strcmp(key, "general.name") == 0) {
            details.name = gguf_kv_to_str(ctx, i).c_str();
        }

        if (strstr(key, "context_length") != nullptr) {
            details.context_length = gguf_kv_to_str(ctx, i).c_str();
        }
    }
    gguf_free(ctx);
    return details;
}

void Inference::check_context_and_resize(int n_batch) {
    const int n_ctx = llama_n_ctx(ctx);
    const int n_used = llama_kv_self_used_cells(ctx);

    if (n_used + n_batch > n_ctx) {
        const int n_discard = (n_used - n_keep) / 2;

        if (n_discard > 0) {
            llama_kv_self_seq_rm(ctx, 0, n_keep, n_keep + n_discard);
            llama_kv_self_seq_add(ctx, 0, n_keep + n_discard, n_used, -n_discard);

            printf("Context resized:\n");
            printf("  - Kept: %d tokens\n", n_keep);
            printf("  - Discarded: %d tokens\n", n_discard);
            printf("  - Remaining: %d tokens\n", llama_kv_self_used_cells(ctx));
        }
    }
}

void Inference::set_sampling_params(const sampling_settings settings) {
    auto sparams = llama_sampler_chain_default_params();
    sparams.no_perf = true;
    smpl = llama_sampler_chain_init(sparams);
    //llama_sampler_chain_add(smpl, llama_sampler_init_greedy());
    llama_sampler_chain_add(smpl, llama_sampler_init_min_p(settings.min_p, 1));
    llama_sampler_chain_add(smpl, llama_sampler_init_temp(settings.temp));
    llama_sampler_chain_add(smpl, llama_sampler_init_top_p(settings.top_p, 1));
    llama_sampler_chain_add(smpl, llama_sampler_init_top_k(settings.top_k));
    llama_sampler_chain_add(smpl, llama_sampler_init_dist(LLAMA_DEFAULT_SEED));
}


std::vector<int32_t> Inference::initialize_batch(const std::string &prompt) {
    llama_batch_free(batch);
    std::vector<llama_token> tokens(llama_n_ctx(ctx));
    int n_tokens = llama_tokenize(
        vocab,
        prompt.c_str(),
        prompt.length(),
        tokens.data(),
        tokens.size(),
        true,   // Add BOS token
        true    // Special tokens allowed
    );

    tokens.resize(n_tokens);
    batch = llama_batch {
        0,
        nullptr,
        nullptr,
        nullptr,
        nullptr,
        nullptr,
        nullptr,
    };

    batch.token = (llama_token *) malloc(sizeof(llama_token) * n_tokens);

    batch.pos      = (llama_pos *)     malloc(sizeof(llama_pos)      * n_tokens);
    batch.n_seq_id = (int32_t *)       malloc(sizeof(int32_t)        * n_tokens);
    batch.seq_id   = (llama_seq_id **) malloc(sizeof(llama_seq_id *) * n_tokens);
    for (int i = 0; i < n_tokens; ++i) {
        batch.seq_id[i] = (llama_seq_id *) malloc(sizeof(llama_seq_id) * 1);
    }
    batch.logits   = (int8_t *)        malloc(sizeof(int8_t)         * n_tokens);

    return tokens;
}

void Inference::completion(
    const std::vector<int32_t> &tokens,
    size_t max_tokens,
    const InferenceCallback &callback,
    void* user_data) {

    generation_cancelled = false;
    common_batch_clear(batch);

    // evaluate the initial prompt
    for (auto i = 0; i < tokens.size(); i++) {
        common_batch_add(batch, tokens[i], i, { 0 }, false);
    }

    // llama_decode will output logits only for the last token of the prompt
    batch.logits[batch.n_tokens - 1] = true;


    if (llama_decode(ctx, batch)) {
        callback("", DECODE_ERROR, user_data);
    }

    // Generate response tokens
    for (size_t i = batch.n_tokens; i <= max_tokens; ++i) {
        if (generation_cancelled){
            callback("", END_OF_GENERATION, user_data);
            break;
        }
        llama_token new_token_id = llama_sampler_sample(smpl, ctx, -1);

        // Check for end of sequence
        if (llama_vocab_is_eog(vocab, new_token_id) || i == max_tokens) {
            callback("", END_OF_GENERATION, user_data);
            break;
        }
        // Decode the token
        auto generated = common_token_to_piece(ctx, new_token_id);
        callback(generated.c_str(), GENERATING, user_data);

        // Prepare batch for next token
        common_batch_clear(batch);
        common_batch_add(batch, new_token_id, i, { 0 }, true);

        if (llama_decode(ctx, batch)) {
            callback("", DECODE_ERROR, user_data);
            break;
        }
    }
}

void Inference::chat(
    const std::string &prompt,
    size_t max_tokens,
    const InferenceCallback &callback,
    void *user_data) {

    callback("", LOADING, user_data);
    generation_cancelled = false;
    const char * tmpl = llama_model_chat_template(model, nullptr);
    messages.push_back({"user", strdup(prompt.c_str())});

    int new_len = llama_chat_apply_template(tmpl, messages.data(), messages.size(), true, formatted.data(), formatted.size());

    if (new_len > (int)formatted.size()) {
        formatted.resize(new_len);
        new_len = llama_chat_apply_template(tmpl, messages.data(), messages.size(), true, formatted.data(), formatted.size());
    }
    if (new_len < 0) {
        fprintf(stderr, "failed to apply the chat template\n");
    }

    std::string formatted_prompt(formatted.begin() + prev_len, formatted.begin() + new_len);


    auto generate = [&](const std::string & prompt) {

        std::string response;

        const bool is_first = llama_kv_self_used_cells(ctx) == 0;

        // tokenize the prompt
        const int n_prompt_tokens = -llama_tokenize(vocab, prompt.c_str(), prompt.size(), NULL, 0, is_first, true);
        std::vector<llama_token> prompt_tokens(n_prompt_tokens);
        if (llama_tokenize(vocab, prompt.c_str(), prompt.size(), prompt_tokens.data(), prompt_tokens.size(), is_first, true) < 0) {
            callback("", TOKENIZE_ERROR, user_data);
        }


        // prepare a batch for the prompt
        llama_batch batch = llama_batch_get_one(prompt_tokens.data(), prompt_tokens.size());
        llama_token new_token_id;
        int count = 0;
        while (true) {
            if (generation_cancelled){
                callback("", END_OF_GENERATION, user_data);
                break;
            }

            check_context_and_resize(batch.n_tokens);

            // check if we have enough space in the context to evaluate this batch
            int n_ctx = llama_n_ctx(ctx);
            int n_ctx_used = llama_kv_self_used_cells(ctx);
            if (n_ctx_used + batch.n_tokens > n_ctx) {
                callback("", CONTEXT_EXCEEDED_ERROR, user_data);
                break;
            }

            if (llama_decode(ctx, batch)) {
                callback("", DECODE_ERROR, user_data);
                break;
            }

            // sample the next token
            new_token_id = llama_sampler_sample(smpl, ctx, -1);

            // is it an end of generation?
            if (llama_vocab_is_eog(vocab, new_token_id)) {
                callback("", END_OF_GENERATION, user_data);
                break;
            }

            // convert the token to a string, print it and add it to the response
            char buf[256];
            int n = llama_token_to_piece(vocab, new_token_id, buf, sizeof(buf), 0, true);
            if (n < 0) {
                callback("", DECODE_ERROR, user_data);
                break;
            }
            std::string piece(buf, n);
            callback(piece.c_str(), GENERATING, user_data);
            count++;
            response += piece;

            if (count >= max_tokens && max_tokens > 0) {
                callback("", END_OF_GENERATION, user_data);
                break;
            }
            generated++;

            batch = llama_batch_get_one(&new_token_id, 1);
        }

        return response;
    };

    std::string response = generate(formatted_prompt);


    messages.push_back({"assistant", strdup(response.c_str())});
    prev_len = llama_chat_apply_template(tmpl, messages.data(), messages.size(), false, nullptr, 0);

}

void Inference::cancel() {
    generation_cancelled = true;
}


void Inference::clean_up() {
    llama_backend_free();
    reset();
}

void Inference::reset() {
    if (!messages.empty()) {
        for (auto &msg: messages) {
            free(const_cast<char *>(msg.content));
        }
    }
    if (ctx) llama_free(ctx);
    if (model) llama_model_free(model);
    if (smpl) llama_sampler_free(smpl);
    llama_batch_free(batch);
}




// int main() {
//
//
//     //Inference inference;
//     model_settings settings;
//     settings.model_path = "/Users/ayodelekehinde/Desktop/DevAssets/DeepSeek-R1-Distill-Qwen-1.5B-Q4_K_M.gguf";
//     settings.callback = [](float progress, void* user_data) -> bool {
//         // Example: Print the progress
//         std::cout << "Progress: " << progress * 100 << "%" << std::endl;
//
//         // End the process when progress reaches 100%
//         return true;
//     };
//     settings.number_of_gpu_layers = 99;
//     settings.use_mlock = false;
//     settings.use_mmap = false;
//
//     auto inferptr = init();
//     load_model(
//         inferptr,
//         settings.model_path.c_str(),
//         settings.number_of_gpu_layers,
//         settings.use_mmap,
//         settings.use_mlock,
//         settings.callback,
//         nullptr
//         );
//
//
//     set_context_params(inferptr, -1, 4096, 512);
//
//     //inference.load_model(settings);
//     sampling_settings sampling_settings;
//     set_sampling_params(inferptr, sampling_settings.temp, sampling_settings.top_p, sampling_settings.min_p, sampling_settings.top_k);
//
//     auto on_generate = [](const char * result, generation_event event, void* user_data) {
//         if (event == END_OF_GENERATION) {
//             printf("Done..\n");
//         }else if (event == CONTEXT_EXCEEDED_ERROR) {
//             printf("Context exceeded error\n");
//         }else if (event == DECODE_ERROR) {
//             printf("Decode error\n");
//         }
//       printf(result);
//     };
//
//     // auto model = "/Users/ayodelekehinde/Desktop/DevAssets/gemma-2-2b-it-Q4_K_M.gguf";
//     // auto details = Inference::get_model_details(model_settings{ model });
//
//     while (true) {
//         // get user input
//         printf("\033[32m> \033[0m");
//         std::string user;
//         std::getline(std::cin, user);
//
//         if (user.empty()) {
//             break;
//         }
//         chat(inferptr, user.c_str(), -1, on_generate, nullptr);
//     }
//
//  }
