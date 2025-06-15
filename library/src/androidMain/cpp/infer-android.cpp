//
// Created by Ayodele Kehinde on 14/01/2025.
//

#include <android/log.h>
#include <jni.h>
#include <bits/sysconf.h>
#include <__algorithm/min.h>
#include "inferkt.h"
#include <iostream>
#include <thread>

struct InferContext {
    JNIEnv*   env;
    jobject   callback;
    jmethodID methodID;
};


extern "C"
JNIEXPORT jlong JNICALL
Java_com_dilivva_inferkt_InferNativeKt_init(JNIEnv *env, jclass clazz) {
    __android_log_print(ANDROID_LOG_DEBUG, "INFERKT", "System properties: %s", llama_print_system_info());
    return init();
}

bool model_load_progress(float progress, void* user_data) {
    auto* ctx = static_cast<InferContext*>(user_data);
    ctx->env->CallVoidMethod(ctx->callback, ctx->methodID, progress);
    if (progress >= 1.0f) {
        ctx->env->DeleteLocalRef(ctx->callback);
        delete ctx;
    }
    return true;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_dilivva_inferkt_InferNativeKt_loadModel(JNIEnv *env,
                        jclass clazz,
                        jlong inference_ptr,
                        jstring path,
                        jint num_of_gpu,
                        jboolean use_mmap,
                        jboolean use_mlock,
                        jobject callback
                        ) {
    auto path_to_model = env->GetStringUTFChars(path, 0);
    jclass callbackClass = env->GetObjectClass(callback);
    jmethodID onProgressCallback = env->GetMethodID(callbackClass, "onProgressCallback", "(F)V");
    if (onProgressCallback == nullptr) {
        __android_log_print(ANDROID_LOG_ERROR, "INFERKT", "Method onProgressCallback not found");
        return false;
    }
    auto* ctx = new InferContext{env, callback, onProgressCallback };
    auto is_model_loaded = load_model(
            inference_ptr,
            path_to_model,
            num_of_gpu,
            use_mmap,
            use_mlock,
            model_load_progress,
            ctx
    );
    env->ReleaseStringUTFChars(path, path_to_model);

    return is_model_loaded;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_dilivva_inferkt_InferNativeKt_setSamplingParams(JNIEnv *env,
                                jclass clazz,
                                jlong inference_ptr,
                                jfloat temp,
                                jfloat top_p,
                                jfloat min_p,
                                jint top_k) {
    set_sampling_params(
            inference_ptr,
            temp,
            top_p,
            min_p,
            top_k
    );
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_dilivva_inferkt_InferNativeKt_setContextParams(JNIEnv *env,
                               jclass clazz,
                               jlong inference_ptr,
                               jint context_window,
                               jint batch,
                               jint num_of_threads) {
    if (num_of_threads == 0){
        num_of_threads = std::max(1, std::min(8, (int) sysconf(_SC_NPROCESSORS_ONLN) - 2));
    }
    return set_context_params(
            inference_ptr,
            context_window,
            batch,
            num_of_threads
    );
}


void generation_callback(const char *message, enum generation_event event, void *user_data) {
    auto* ctx = static_cast<InferContext*>(user_data);
    jstring messageStr = ctx->env->NewStringUTF(message);
    jint j_event = static_cast<jint>(event);
    ctx->env->CallVoidMethod(ctx->callback, ctx->methodID, messageStr, j_event);
    ctx->env->DeleteLocalRef(messageStr);
    if (event != LOADING && event != GENERATING){
        ctx->env->DeleteLocalRef(ctx->callback);
        delete ctx;
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_com_dilivva_inferkt_InferNativeKt_completion(JNIEnv *env,
                                                  jclass clazz,
                                                  jlong inference_ptr,
                                                  jstring prompt,
                                                  jint max_generation_count,
                                                  jobject callback) {

    jclass callbackClass = env->GetObjectClass(callback);
    jmethodID onGenerationMethod = env->GetMethodID(callbackClass, "onGeneration", "(Ljava/lang/String;I)V");
    if (onGenerationMethod == nullptr) {
        __android_log_print(ANDROID_LOG_ERROR, "INFERKT", "Method onGeneration not found");
        return;
    }

    auto prompt_char = env->GetStringUTFChars(prompt, 0);
    __android_log_print(ANDROID_LOG_ERROR, "INFERKT", "Prompt: %s", prompt_char);
    __android_log_print(ANDROID_LOG_DEBUG, "INFERKT", "Infering...");
    auto * ctx = new InferContext{env, callback, onGenerationMethod };
    complete(inference_ptr, prompt_char, max_generation_count, generation_callback, ctx);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_dilivva_inferkt_InferNativeKt_chat(JNIEnv *env,
                                                  jclass clazz,
                                                  jlong inference_ptr,
                                                  jstring prompt,
                                                  jint max_generation_count,
                                                  jobject callback) {

    jclass callbackClass = env->GetObjectClass(callback);
    jmethodID onGenerationMethod = env->GetMethodID(callbackClass, "onGeneration", "(Ljava/lang/String;I)V");
    if (onGenerationMethod == nullptr) {
        __android_log_print(ANDROID_LOG_ERROR, "INFERKT", "Method onGeneration not found");
        return;
    }

    auto prompt_char = env->GetStringUTFChars(prompt, 0);
    __android_log_print(ANDROID_LOG_ERROR, "INFERKT", "Prompt: %s", prompt_char);
    auto * ctx = new InferContext{env, callback, onGenerationMethod };
    chat(inference_ptr, prompt_char, max_generation_count, generation_callback, ctx);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_dilivva_inferkt_InferNativeKt_getModelDetails(JNIEnv *env,
                                            jclass clazz,
                                            jstring path) {

    auto path_to_model = env->GetStringUTFChars(path, 0);
    auto model_details = get_model_details(path_to_model);

    jclass modelDetails = env->FindClass("com/dilivva/inferkt/ModelDetails");
    if (!modelDetails) return nullptr;

    jmethodID ctor = env->GetMethodID(modelDetails, "<init>", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    if (!ctor) {
        env->DeleteLocalRef(modelDetails);
        return nullptr;
    }

    jstring name = env->NewStringUTF(model_details.name);
    jstring arch = env->NewStringUTF(model_details.architecture);
    jstring context_length = env->NewStringUTF(model_details.context_length);
    jint   version = model_details.version;

    jobject dataObj = env->NewObject(modelDetails, ctor, version, arch, name, context_length);

    env->DeleteLocalRef(name);
    env->DeleteLocalRef(arch);
    env->DeleteLocalRef(context_length);
    env->DeleteLocalRef(modelDetails);

    return dataObj;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_dilivva_inferkt_InferNativeKt_cancelGeneration(JNIEnv *env,jclass clazz,jlong inference_ptr) {
    cancel_inference(inference_ptr);
}