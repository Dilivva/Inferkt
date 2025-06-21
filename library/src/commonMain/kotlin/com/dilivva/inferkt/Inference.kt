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

interface Inference {
    /**
     * Preloads a model with the given settings.
     *
     * @param modelSettings The settings for the model to be preloaded.
     * @param progressCallback A callback function to report the progress of model loading.
     *                         It takes a Float representing the progress (0.0 to 1.0) and returns a Boolean.
     *                         If the callback returns `false`, the loading process will be cancelled.
     * @return `true` if the model was preloaded successfully, `false` otherwise.
     */
    fun preloadModel(modelSettings: ModelSettings, progressCallback: (Float) -> Boolean): Boolean

    /**
     * Sets the sampling parameters for the inference.
     *
     * @param samplingSettings The sampling settings to be used.
     */
    fun setSamplingParams(samplingSettings: SamplingSettings)

    /**
     * Performs completion given a prompt.
     *
     * @param prompt The input prompt for the completion.
     * @param maxTokens The maximum number of tokens to generate.
     * @param onGenerate A callback function that is invoked with [GenerationEvent]s during the generation process.
     */
    fun completion(prompt: String, maxTokens: Int, onGenerate: (GenerationEvent) -> Unit)

    /**
     * Performs chat completion given a prompt.
     * This is typically used for conversational models.
     */
    fun chat(prompt: String, maxTokens: Int, onGenerate: (GenerationEvent) -> Unit)

    /**
     * Cancels the current generation process.
     */
    fun cancelGeneration()

    /**
     * Retrieves details about a model located at the specified path.
     *
     * @param path The absolute path to the model file.
     * @return [ModelDetails] containing information about the model.
     */
    fun getModelDetails(path: String): ModelDetails

    /**
     * Releases any resources held by the inference engine.
     */
    fun release()
}

expect fun createInference(): Inference