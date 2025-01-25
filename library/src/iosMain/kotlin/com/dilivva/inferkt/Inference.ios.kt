package com.dilivva.inferkt

import dilivva.init
import dilivva.load_model
import dilivva.set_context_params
import dilivva.set_sampling_params
import kotlinx.cinterop.ByteVarOf
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString


actual fun createInference(): Inference {
    return IosInference()
}

@OptIn(ExperimentalForeignApi::class)
class IosInference: Inference{

    private val inferencePtr = init()

    override fun preloadModel(path: String): Boolean {
        return load_model(inferencePtr, path, true)
    }

    override fun generate(prompt: String, maxTokens: Int, onGenerate: (String) -> Unit) {
        set_context_params(inferencePtr, 512, 512)
        set_sampling_params(inferencePtr, 0.8f, 0.95f, 40)
        val callback = staticCFunction<CPointer<ByteVarOf<Byte>>?, Boolean, Unit>{ textBytes, isComplete ->
            val text = textBytes?.toKString()
            if (!isComplete && text != null){
                onGenerate(text)
            }
        }
        dilivva.generate(inferencePtr, prompt, maxTokens, callback)
    }

}