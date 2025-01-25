//
// Created by Ayodele Kehinde on 14/01/2025.
//

#include <android/log.h>
#include <jni.h>
#include <bits/sysconf.h>
#include <__algorithm/min.h>
#include "inferkt.h"


extern "C"
JNIEXPORT jlong JNICALL
Java_com_dilivva_inferkt_InferNativeKt_init(JNIEnv *env, jclass clazz) {
    return init();
}



extern "C"
JNIEXPORT jboolean JNICALL
Java_com_dilivva_inferkt_InferNativeKt_loadModel(JNIEnv *env, jclass clazz, jlong inference_ptr,
                                                 jstring path) {
    auto path_to_model = env->GetStringUTFChars(path, 0);
    auto model = load_model(inference_ptr,path_to_model, false);
    env->ReleaseStringUTFChars(path, path_to_model);

    return model;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_dilivva_inferkt_InferNativeKt_setSamplingParams(JNIEnv *env, jclass clazz,
                                                         jlong inference_ptr, jfloat temp,
                                                         jfloat top_p, jint top_k) {
    set_sampling_params(inference_ptr, temp, top_p, top_k);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_dilivva_inferkt_InferNativeKt_setContextParams(JNIEnv *env, jclass clazz,
                                                        jlong inference_ptr, jint context_window,
                                                        jint batch) {
    int n_threads = std::max(1, std::min(8, (int) sysconf(_SC_NPROCESSORS_ONLN) - 2));
    __android_log_print(ANDROID_LOG_DEBUG, "INFERKT", "Threads=%i",n_threads);
    set_context_params(inference_ptr, context_window, batch);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_dilivva_inferkt_InferNativeKt_generate(JNIEnv *env, jclass clazz, jlong inference_ptr,
                                                jstring prompt, jint max_generation_count,
                                                jobject callback) {

    __android_log_print(ANDROID_LOG_DEBUG, "INFERKT", "Generating...");

    jclass callbackClass = env->GetObjectClass(callback);
    jmethodID onGenerationMethod = env->GetMethodID(callbackClass, "onGeneration", "(Ljava/lang/String;)V");
    if (onGenerationMethod == nullptr) {
        __android_log_print(ANDROID_LOG_ERROR, "INFERKT", "Method onGeneration not found");
        return;
    }

    auto prompt_char = env->GetStringUTFChars(prompt, 0);
    __android_log_print(ANDROID_LOG_ERROR, "INFERKT", "Prompt: %s", prompt_char);
    auto m_callback = [env, callback, onGenerationMethod](const char* token_text, bool is_complete) {
        //__android_log_print(ANDROID_LOG_ERROR, "INFERKT", "Gen: %b", is_complete);
        if (!is_complete){
            jstring messageStr = env->NewStringUTF(token_text);
            env->CallVoidMethod(callback, onGenerationMethod, messageStr);
            env->DeleteLocalRef(messageStr);
        }
    };
    __android_log_print(ANDROID_LOG_DEBUG, "INFERKT", "Infering...");
    infer(inference_ptr, prompt_char, max_generation_count, m_callback);
}