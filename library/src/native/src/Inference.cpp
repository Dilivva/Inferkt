//
// Created by Ayodele Kehinde on 21/01/2025.
//

#include "Inference.h"

#include <thread>
#include <unistd.h>

#include "common.h"

Inference::Inference() {
    llama_backend_init();
}

Inference::~Inference() {
    cleanUp();
}

bool Inference::loadModel(const std::string &model_path, bool use_gpu) {
    // Step 1: Initialize model parameters
    llama_model_params model_params = llama_model_default_params();
    if (use_gpu){
        model_params.n_gpu_layers = 99;
    } else{
        model_params.n_gpu_layers = 0;
    }

    //model_params.use_mlock = true;

    // Load the model
    model = llama_model_load_from_file(model_path.c_str(), model_params);
    if (!model) {
        return false;
    }

    return true;
}

void Inference::setSamplingParams(const float temp, const float top_p, const int32_t top_k) {
    auto sparams = llama_sampler_chain_default_params();
    sparams.no_perf = true;
    smpl = llama_sampler_chain_init(sparams);
    llama_sampler_chain_add(smpl, llama_sampler_init_greedy());
    // llama_sampler_chain_add(smpl, llama_sampler_init_min_p(0.05f, 1));
    // llama_sampler_chain_add(smpl, llama_sampler_init_temp(temp));
    // llama_sampler_chain_add(smpl, llama_sampler_init_top_p(top_p, 1));
    // llama_sampler_chain_add(smpl, llama_sampler_init_top_k(top_k));
    // llama_sampler_chain_add(smpl, llama_sampler_init_dist(LLAMA_DEFAULT_SEED));
}

bool Inference::setContextParams(int context_window, int batch) {
    int max_threads = std::thread::hardware_concurrency();
    // Use 2 threads by default on 4-core devices, 4 threads on more cores
    int default_n_threads = max_threads == 4 ? 2 : std::min(4, max_threads);
    llama_context_params ctx_params = llama_context_default_params();
    ctx_params.n_ctx = context_window;  // Context window size
    ctx_params.n_batch = batch;
    ctx_params.n_ubatch = batch;
    ctx_params.n_threads = default_n_threads;
    ctx_params.n_threads_batch = default_n_threads;

    // Create context
    ctx = llama_init_from_model(model, ctx_params);
    vocab = llama_model_get_vocab(model);
    return ctx != nullptr && vocab != nullptr;
}

std::vector<int32_t> Inference::initializeBatch(const std::string &prompt) {
    delete batch;
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
    batch = new llama_batch {
        0,
        nullptr,
        nullptr,
        nullptr,
        nullptr,
        nullptr,
        nullptr,
    };

    batch->token = (llama_token *) malloc(sizeof(llama_token) * n_tokens);

    batch->pos      = (llama_pos *)     malloc(sizeof(llama_pos)      * n_tokens);
    batch->n_seq_id = (int32_t *)       malloc(sizeof(int32_t)        * n_tokens);
    batch->seq_id   = (llama_seq_id **) malloc(sizeof(llama_seq_id *) * n_tokens);
    for (int i = 0; i < n_tokens; ++i) {
        batch->seq_id[i] = (llama_seq_id *) malloc(sizeof(llama_seq_id) * 1);
    }
    batch->logits   = (int8_t *)        malloc(sizeof(int8_t)         * n_tokens);

    return tokens;
}

void Inference::runInference(
    const std::vector<int32_t> &tokens,
    size_t max_tokens,
    const InferenceCallback &callback) const {

    for (auto token : tokens) {
        printf("%s",common_token_to_piece(ctx, token).c_str());
    }

    common_batch_clear(*batch);

    // evaluate the initial prompt
    for (auto i = 0; i < tokens.size(); i++) {
        common_batch_add(*batch, tokens[i], i, { 0 }, false);
    }

    // llama_decode will output logits only for the last token of the prompt
    batch->logits[batch->n_tokens - 1] = true;


    if (llama_decode(ctx, *batch)) {
        callback("", true);
    }

    // Generate response tokens
    for (size_t i = batch->n_tokens; i <= max_tokens; ++i) {

        llama_token new_token_id = llama_sampler_sample(smpl, ctx, -1);

        // Check for end of sequence
        if (llama_vocab_is_eog(vocab, new_token_id) || i == max_tokens) {
            callback("", true);
            break;
        }

        // Decode the token
        auto generated = common_token_to_piece(ctx, new_token_id);
        callback(generated.c_str(), false);

        // Prepare batch for next token
        common_batch_clear(*batch);
        common_batch_add(*batch, new_token_id, i, { 0 }, true);

        if (llama_decode(ctx, *batch)) {
            callback("", true);
            break;
        }
    }
}

void Inference::cleanUp() const {
    llama_backend_free();
    if (ctx) llama_free(ctx);
    if (model) llama_model_free(model);
    if (smpl) llama_sampler_free(smpl);
    delete batch;
}



//int main() {
//    Inference inference;
//    inference.loadModel("/Users/ayodelekehinde/Desktop/DevAssets/DeepSeek-R1-Distill-Qwen-1.5B-Q4_K_M.gguf");
//    inference.setSamplingParams();
//    inference.setContextParams();
//    const char *prompt = "What is democracy?";
//    auto tokens = inference.initializeBatch(prompt);
//    auto on_generate = [](std::string result, bool is_complete) {
//      printf(result.c_str());
//    };
//    inference.runInference(tokens, 30, on_generate);
//}