package com.dilivva.inferkt

interface Inference {
    fun preloadModel(modelSettings: ModelSettings, progressCallback: (Float) -> Boolean): Boolean
    fun setSamplingParams(samplingSettings: SamplingSettings)
    fun completion(prompt: String, maxTokens: Int, onGenerate: (GenerationEvent) -> Unit)
    fun chat(prompt: String, maxTokens: Int, onGenerate: (GenerationEvent) -> Unit)
    fun cancelGeneration()
    fun getModelDetails(path: String): ModelDetails
}

expect fun createInference(): Inference