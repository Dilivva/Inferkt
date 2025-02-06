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

extern "C" bool load_model(const long inference_ptr, const char *file_path, bool use_gpu) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    const auto is_loaded = inference->loadModel(file_path, use_gpu);
    return is_loaded;
}

extern "C" void set_sampling_params(const long inference_ptr, const float temp, const float top_p, const int32_t top_k) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    inference->setSamplingParams(temp, top_p, top_k);
}

extern "C" bool set_context_params(const long inference_ptr, const int context_window, const int batch) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    return inference->setContextParams(context_window, batch);
}

void infer(const long inference_ptr, const char *prompt, const int max_generation_count, const InferenceCallback &callback) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    const std::vector<int32_t> tokens = inference->initializeBatch(prompt);
    inference->runInference(tokens, max_generation_count, callback);
}

extern "C" void clean_up(const long inference_ptr) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    inference->cleanUp();
    delete inference;
}

extern "C" void generate(long inference_ptr, const char *prompt, int max_generation_count, GenerationCCallback callback, void* user_data) {
    const auto inference = reinterpret_cast<Inference *>(inference_ptr);
    const std::vector<int32_t> tokens = inference->initializeBatch(prompt);
    inference->runInference(tokens, max_generation_count, callback, user_data);
}
