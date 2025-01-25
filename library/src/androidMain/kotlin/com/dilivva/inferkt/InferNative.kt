package com.dilivva.inferkt


interface Callback {
    fun onGeneration(text: String)
}


external fun init(): Long

external fun loadModel(inferencePtr: Long, path: String): Boolean

external fun setSamplingParams(inferencePtr: Long, temp: Float, topP: Float,  topK: Int)

external fun setContextParams(inferencePtr: Long, contextWindow: Int, batch: Int)

external fun generate(inferencePtr: Long, prompt: String, maxGenerationCount: Int, callback: Callback)