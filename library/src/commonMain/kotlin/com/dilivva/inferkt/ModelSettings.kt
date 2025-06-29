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
 * Configuration settings for the language model.
 *
 * @property modelPath The file path to the language model.
 * @property numberOfGpuLayers The number of GPU layers to use for computation. Defaults to 0 (CPU only).
 * @property useMmap Whether to use memory mapping for model loading. Defaults to true.
 * @property useMlock Whether to lock the model in memory. Defaults to half of the total threads.
 * @property numberOfThreads The number of threads to use for inference. Defaults to -1 (auto-detect).
 * @property context The context window size for the model. Defaults to 512. Setting higher context sizes may result in out-of-memory errors.
 * @property batchSize The batch size for processing. Defaults to 512.
 */
data class ModelSettings(
    val modelPath: String,
    val numberOfGpuLayers: Int = 0,
    val useMmap: Boolean = true,
    val useMlock: Boolean = true,
    val numberOfThreads: Int = -1,
    val context: Int = 512,
    val batchSize: Int = 512
)




