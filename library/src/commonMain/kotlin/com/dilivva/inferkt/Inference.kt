package com.dilivva.inferkt

interface Inference {
    fun preloadModel(path: String): Boolean
    fun generate(prompt: String, maxTokens: Int, onGenerate: (String) -> Unit)
}

interface Generator: AutoCloseable{

}


class AndroidGen: Generator{
    override fun close() {
        TODO("Not yet implemented")
    }
}

expect fun createInference(): Inference