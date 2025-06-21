package com.dilivva.inferkt

import dilivva.clean_up
import dilivva.generation_event
import dilivva.init
import dilivva.load_model
import dilivva.set_context_params
import dilivva.set_sampling_params
import kotlinx.cinterop.ByteVarOf
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointed
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString
import kotlinx.cinterop.useContents


actual fun createInference(): Inference {
    return IosInference
}

@OptIn(ExperimentalForeignApi::class)
object IosInference: Inference{

    private val inferencePtr = init()
    private lateinit var modelSettings: ModelSettings

    override fun preloadModel(modelSettings: ModelSettings, progressCallback: (Float) -> Boolean): Boolean {
        this.modelSettings = modelSettings
        val onProgressRef = StableRef.create(progressCallback)
        val callback = staticCFunction<Float, CPointer<out CPointed>?, Boolean>{ progress, userData ->
            val userDataRef = userData?.asStableRef<(Float) -> Boolean>()?.get()
            val progressPercent = progress * 100
            userDataRef?.invoke(progressPercent) ?: true
        }

        val isModelLoaded =  load_model(
            inference_ptr = inferencePtr,
            model_path = modelSettings.modelPath,
            number_of_gpu_layers = modelSettings.numberOfGpuLayers,
            use_mmap = modelSettings.useMmap,
            use_mlock = modelSettings.useMlock,
            callback = callback,
            user_data = onProgressRef.asCPointer()
        )
        if (!isModelLoaded) return false

        onProgressRef.dispose()
        return set_context_params(
            inference_ptr = inferencePtr,
            context_length = modelSettings.context,
            batch = modelSettings.batchSize,
            number_of_threads = modelSettings.numberOfThreads
        )
    }
    


    override fun setSamplingParams(samplingSettings: SamplingSettings) {
        set_sampling_params(
            inference_ptr = inferencePtr,
            temp = samplingSettings.temp,
            top_p = samplingSettings.topP,
            min_p = samplingSettings.minP,
            top_k = samplingSettings.topK
        )
    }

    override fun completion(prompt: String, maxTokens: Int, onGenerate: (GenerationEvent) -> Unit) {
        inference(
            onGenerate = onGenerate,
            onOperation = { ref, callback ->
                dilivva.complete(
                    inference_ptr = inferencePtr,
                    prompt = prompt,
                    max_generation_count = maxTokens,
                    callback = callback,
                    user_data = ref.asCPointer()
                )
            }
        )
    }

    override fun chat(prompt: String, maxTokens: Int, onGenerate: (GenerationEvent) -> Unit) {
        inference(
            onGenerate = onGenerate,
            onOperation = { ref, callback ->
                dilivva.chat(
                    inference_ptr = inferencePtr,
                    prompt = prompt,
                    max_generation_count = maxTokens,
                    callback = callback,
                    user_data = ref.asCPointer()
                )
            }
        )
    }

    override fun cancelGeneration() {
        dilivva.cancel_inference(inferencePtr)
    }

    override fun getModelDetails(path: String): ModelDetails {
        return dilivva.get_model_details(path).useContents {
            ModelDetails(
                version = this.version,
                architecture = this.architecture?.toKString().orEmpty(),
                name = name?.toKString().orEmpty(),
                contextLength = context_length?.toKString().orEmpty()
            )
        }
    }

    override fun release() {
        clean_up(inferencePtr)
    }

    private fun inference(
        onGenerate: (GenerationEvent) -> Unit,
        onOperation: (StableRef<(GenerationEvent) -> Unit>,  CPointer<CFunction<(CPointer<ByteVarOf<Byte>>?, generation_event, CPointer<out CPointed>?) -> Unit>>) -> Unit
    ){
        val onGenerateRef = StableRef.create(onGenerate)
        val callback = staticCFunction<CPointer<ByteVarOf<Byte>>?, generation_event, CPointer<out CPointed>?, Unit>{ textBytes, event, userData ->
            val text = textBytes?.toKString()

            if(text != null){
                val userDataRef = userData?.asStableRef<(GenerationEvent) -> Unit>()?.get()
                when(event){
                    generation_event.CONTEXT_EXCEEDED_ERROR -> {
                        userDataRef?.invoke(GenerationEvent.Error(GenerationError.CONTEXT_EXCEEDED_ERROR))
                    }
                    generation_event.END_OF_GENERATION -> {
                        userDataRef?.invoke(GenerationEvent.Generated)
                    }
                    generation_event.DECODE_ERROR -> {
                        userDataRef?.invoke(GenerationEvent.Error(GenerationError.DECODE_ERROR))
                    }
                    generation_event.TOKENIZE_ERROR -> {
                        userDataRef?.invoke(GenerationEvent.Error(GenerationError.TOKENIZE_ERROR))
                    }
                    generation_event.GENERATING -> {
                        userDataRef?.invoke(GenerationEvent.Generating(text))
                    }
                    generation_event.LOADING -> {
                        userDataRef?.invoke(GenerationEvent.Loading)
                    }
                    else -> {
                        userDataRef?.invoke(GenerationEvent.Error(GenerationError.UNKNOWN))
                    }
                }
            }
        }
        onOperation(onGenerateRef, callback)
        onGenerateRef.dispose()
    }

}