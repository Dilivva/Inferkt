#include <functional>

#include <string>
#include <vector>

#include "common.h"
#include "Inference.h"
#include "inferkt.h"

extern "C" long init() {
    auto inference = new Inference();
    return reinterpret_cast<long>(inference);
}

extern "C" bool load_model(long inference_ptr,
                           const char* model_path,
                           int number_of_gpu_layers,
                           bool use_mmap,
                           bool use_mlock,
                           progress_callback callback,
                           void *user_data) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    model_settings settings{
        model_path,
        number_of_gpu_layers,
        use_mmap,
        use_mlock,
        callback,
        user_data
    };
    return inference->load_model(settings);
}

extern "C" bool set_context_params(long inference_ptr, int context_length, int batch, int number_of_threads) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    return inference->init_context(context_length, batch, number_of_threads);
}

extern "C" void set_sampling_params(long inference_ptr,
                                    float temp,
                                    float top_p,
                                    float min_p,
                                    int32_t top_k) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    sampling_settings settings{
        temp,
        top_p,
        min_p,
        top_k,
    };
    inference->set_sampling_params(settings);
}

void cxx_complete(const long inference_ptr, const char *prompt, const int max_generation_count, const InferenceCallback &callback) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    const std::vector<int32_t> tokens = inference->initialize_batch(prompt);
    inference->completion(tokens, max_generation_count, callback);
}

extern "C" void clean_up(const long inference_ptr) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    delete inference;
}

extern "C" void complete(long inference_ptr, const char *prompt, int max_generation_count, GenerationCCallback callback, void* user_data) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    const std::vector<int32_t> tokens = inference->initialize_batch(prompt);
    inference->completion(tokens, max_generation_count, callback, user_data);
}

extern "C" void cancel_inference(long inference_ptr){
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    inference->cancel();
}

extern "C" void chat(long inference_ptr, const char *prompt, int max_generation_count, GenerationCCallback callback, void *user_data) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    inference->chat(prompt, max_generation_count, callback, user_data);
}

extern "C" model_details get_model_details(const char* model_path) {
    return Inference::get_model_details(model_settings{ model_path });
}




