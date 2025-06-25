package com.dilivva.inferkt

actual fun createInference(): Inference {
    return AndroidInference
}

object AndroidInference: Inference{

    init {
        System.loadLibrary("inferkt-android")
    }
    private val inferPtr = init()
    private lateinit var modelSettings: ModelSettings

    override fun preloadModel(
        modelSettings: ModelSettings,
        progressCallback: (Float) -> Boolean
    ): Boolean {
        this.modelSettings = modelSettings
        val callback = object: Callback {
            override fun onGeneration(text: String, event: Int) {}
            override fun onProgressCallback(progress: Float) {
                progressCallback(progress)
            }
        }
        val isModelLoaded = loadModel(
            inferencePtr = inferPtr,
            path = modelSettings.modelPath,
            numberOfGpu = modelSettings.numberOfGpuLayers,
            useMmap = modelSettings.useMmap,
            useMlock = modelSettings.useMlock,
            callback = callback
        )
        if (!isModelLoaded) return false

        return setContextParams(
            inferencePtr = inferPtr,
            contextWindow = modelSettings.context,
            batch = modelSettings.batchSize,
            numberOfThreads = modelSettings.numberOfThreads
        )
    }

    override fun setSamplingParams(samplingSettings: SamplingSettings) {
        setSamplingParams(
            inferencePtr = inferPtr,
            temp = samplingSettings.temp,
            topP = samplingSettings.topP,
            minP = samplingSettings.minP,
            topK = samplingSettings.topK
        )
    }

    override fun completion(prompt: String, maxTokens: Int, onGenerate: (GenerationEvent) -> Unit) {
        inference(
            onGenerate = onGenerate,
            onOperation = {
                completion(
                    inferencePtr = inferPtr,
                    prompt = prompt,
                    maxGenerationCount = maxTokens,
                    callback = it
                )
            }
        )
    }

    override fun chat(prompt: String, maxTokens: Int, onGenerate: (GenerationEvent) -> Unit) {
        inference(
            onGenerate = onGenerate,
            onOperation = {
                chat(
                    inferencePtr = inferPtr,
                    prompt = prompt,
                    maxGenerationCount = maxTokens,
                    callback = it
                )
            }
        )
    }

    override fun cancelGeneration() {
        cancelGeneration(inferPtr)
    }

    override fun getModelDetails(path: String): ModelDetails {
        return com.dilivva.inferkt.getModelDetails(path)
    }

    override fun release() {
        cleanUp(inferPtr)
    }

    private fun inference(
        onGenerate: (GenerationEvent) -> Unit,
        onOperation: (Callback) -> Unit
    ){
        val callback = object: Callback {
            override fun onGeneration(text: String, event: Int) {
                when(event){
                    0 -> onGenerate(GenerationEvent.Loading)
                    1 -> onGenerate(GenerationEvent.Generating(text))
                    2 -> onGenerate(GenerationEvent.Error(GenerationError.TOKENIZE_ERROR))
                    3 -> onGenerate(GenerationEvent.Error(GenerationError.CONTEXT_EXCEEDED_ERROR))
                    4 -> onGenerate(GenerationEvent.Error(GenerationError.DECODE_ERROR))
                    5 -> onGenerate(GenerationEvent.Generated)
                }
            }

            override fun onProgressCallback(progress: Float) {}

        }
        onOperation(callback)
    }
}