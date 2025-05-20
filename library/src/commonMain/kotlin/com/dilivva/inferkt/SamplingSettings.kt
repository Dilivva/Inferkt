package com.dilivva.inferkt

data class SamplingSettings(
    val temp: Float = 0.8f,
    val topP: Float = 0.95f,
    val minP: Float = 0.05f,
    val topK: Int = 40
)
