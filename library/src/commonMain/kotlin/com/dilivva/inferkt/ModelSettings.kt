package com.dilivva.inferkt

data class ModelSettings(
    val modelPath: String,
    val numberOfGpuLayers: Int = 0,
    val useMmap: Boolean = true,
    val useMlock: Boolean = true,
    val numberOfThreads: Int = -1,
    val context: Int = 512,
    val batchSize: Int = 512
)
