//
// Created by Ayodele Kehinde on 21/01/2025.
//

#ifndef INFERENCE_H
#define INFERENCE_H
#include <functional>

#include "llama.h"

using InferenceCallback = std::function<void(const char*, bool, void*)>;

class Inference {
    llama_context* ctx = nullptr;
    llama_model* model = nullptr;
    llama_sampler * smpl = nullptr;
    const llama_vocab *vocab = nullptr;
    llama_batch *batch {};

    //void generate(const std::vector<int32_t>& tokens, size_t max_tokens, GenerationCallback callback) const;

public:
    Inference();
    ~Inference();


    bool loadModel(const std::string& model_path, bool use_gpu);
    void setSamplingParams(float temp = 0.8f, float top_p = 0.95f, int32_t top_k = 40);
    bool setContextParams(int context_window = 512, int batch = 512);
    std::vector<int32_t> initializeBatch(const std::string& prompt);
    void runInference(const std::vector<int32_t>& tokens, size_t max_tokens = 128, const InferenceCallback& callback = {}, void* user_data = nullptr) const;
    void cleanUp() const;
};



#endif //INFERENCE_H
