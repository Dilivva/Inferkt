//
// Created by Ayodele Kehinde on 15/01/2025.
//

#ifndef INFERKT_H
#define INFERKT_H

#include "Inference.h"


#ifdef __cplusplus
#include <functional>
//Android interface
using GenerationCallback = std::function<void(const char *, generation_event)>;

extern "C" {
void cxx_complete(long inference_ptr, const char *prompt, int max_generation_count, const GenerationCallback &callback);
#endif

#include <stdbool.h>

typedef void (*GenerationCCallback)(const char *message, enum generation_event event, void *user_data);

long init();

bool load_model(
        long inference_ptr,
        const char* model_path,
        int number_of_gpu_layers,
        bool use_mmap,
        bool use_mlock,
        progress_callback callback,
        void *user_data);

bool set_context_params(long inference_ptr, int context_length, int batch, int number_of_threads);

void set_sampling_params(
        long inference_ptr,
        float temp,
        float top_p,
        float min_p,
        int32_t top_k);

void complete(
        long inference_ptr,
        const char *prompt,
        int max_generation_count,
        GenerationCCallback callback,
        void *user_data);

void chat(
        long inference_ptr,
        const char *prompt,
        int max_generation_count,
        GenerationCCallback callback,
        void *user_data);

void cancel_inference(long inference_ptr);

void clean_up(long inference_ptr);


struct model_details get_model_details(const char* model_path);

#ifdef __cplusplus
}
#endif


//typedef void (*GenerationCCallback)(const char* message, bool is_complete);
//
//long init();
//bool load_model(long inference_ptr, const char *file_path, bool use_gpu);
//void set_sampling_params(long inference_ptr, float temp, float top_p, int top_k);
//bool set_context_params(long inference_ptr, int context_window, int batch);
//void clean_up(long inference_ptr);
//void generate(long inference_ptr, const char *prompt, int max_generation_count, GenerationCCallback callback);


#endif //INFERKT_INFERKT_H
