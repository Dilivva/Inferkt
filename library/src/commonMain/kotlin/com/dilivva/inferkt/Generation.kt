package com.dilivva.inferkt


sealed interface GenerationEvent{
    data class Generating(val text: String): GenerationEvent
    data class Error(val error: GenerationError): GenerationEvent
    data object Generated: GenerationEvent
    data object Loading: GenerationEvent
}

enum class GenerationError{
    TOKENIZE_ERROR,
    CONTEXT_EXCEEDED_ERROR,
    DECODE_ERROR,
    UNKNOWN
}