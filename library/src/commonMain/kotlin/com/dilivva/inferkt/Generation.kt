/*
 * Copyright (C) 2025, Send24.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 * SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.dilivva.inferkt

/**
 * Represents the different states and events that can occur during a text generation process.
 * This sealed interface is used to communicate the progress and outcome of the generation.
 */
sealed interface GenerationEvent{
    /**
     * Indicates that the generation process is currently active and producing text.
     * @property text The chunk of text generated in the current step.
     */
    data class Generating(val text: String): GenerationEvent
    /**
     * Signals that an error occurred during the generation process.
     * @property error The specific [GenerationError] that occurred.
     */
    data class Error(val error: GenerationError): GenerationEvent
    /**
     * Marks the successful completion of the text generation process.
     */
    data object Generated: GenerationEvent
    /**
     * Indicates that the model is currently loading or preparing for generation.
     */
    data object Loading: GenerationEvent
}

/**
 * Enumerates the possible errors that can occur during the text generation process.
 */
enum class GenerationError{
    /** An error occurred during the tokenization of the input text. */
    TOKENIZE_ERROR,
    /** The input context exceeded the maximum allowed length for the model. */
    CONTEXT_EXCEEDED_ERROR,
    /** An error occurred while decoding the generated tokens back into text. */
    DECODE_ERROR,
    /** An unknown or unspecified error occurred. */
    UNKNOWN
}