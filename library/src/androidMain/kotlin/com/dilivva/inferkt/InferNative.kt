package com.dilivva.inferkt


interface Callback {
    fun onGeneration(text: String, event: Int)
    fun onProgressCallback(progress: Float)
}




external fun init(): Long

external fun loadModel(inferencePtr: Long, path: String, numberOfGpu: Int, useMmap: Boolean, useMlock: Boolean, callback: Callback): Boolean

external fun setSamplingParams(inferencePtr: Long, temp: Float, topP: Float, minP: Float, topK: Int)

external fun setContextParams(inferencePtr: Long, contextWindow: Int, batch: Int, numberOfThreads: Int): Boolean

external fun completion(inferencePtr: Long, prompt: String, maxGenerationCount: Int, callback: Callback)

external fun chat(inferencePtr: Long, prompt: String, maxGenerationCount: Int, callback: Callback)

external fun getModelDetails(path: String): ModelDetails

external fun cancelGeneration(inferencePtr: Long)

external fun cleanUp(inferencePtr: Long)