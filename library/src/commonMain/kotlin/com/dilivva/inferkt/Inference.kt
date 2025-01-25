package com.dilivva.inferkt

interface Inference {
    fun preloadModel(path: String): Boolean
    fun generate(prompt: String, maxTokens: Int, onGenerate: (String) -> Unit)
}

expect fun createInference(): Inference