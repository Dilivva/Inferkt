//
// Created by Ayodele Kehinde on 15/01/2025.
//

#ifndef INFERKT_H
#define INFERKT_H
#include <stdbool.h>



#ifdef __cplusplus
#include <functional>
using GenerationCallback = std::function<void(const char*, bool)>;
extern "C" {
    void infer(long inference_ptr, const char *prompt, int max_generation_count,  const GenerationCallback &callback);
}
#endif


typedef void (*GenerationCCallback)(const char* message, bool is_complete);

long init();
bool load_model(long inference_ptr, const char *file_path, bool use_gpu);
void set_sampling_params(long inference_ptr, float temp, float top_p, int top_k);
bool set_context_params(long inference_ptr, int context_window, int batch);
void clean_up(long inference_ptr);
void generate(long inference_ptr, const char *prompt, int max_generation_count, GenerationCCallback callback);


#endif //INFERKT_INFERKT_H
