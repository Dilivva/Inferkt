package com.dilivva.inferkt

actual fun createInference(): Inference {
    return AndroidInference()
}

class AndroidInference: Inference{

    private val inferPtr = init()

    override fun preloadModel(path: String): Boolean {
        return loadModel(inferPtr, path)
    }

    override fun generate(prompt: String, maxTokens: Int, onGenerate: (String) -> Unit) {
        setContextParams(inferPtr, 512, 512)
        setSamplingParams(inferPtr, 0.8f, 0.95f, 40)
        val callback = object: Callback{
            override fun onGeneration(text: String) {
                onGenerate(text)
            }

        }
        generate(inferPtr, prompt, maxTokens, callback)
    }

}