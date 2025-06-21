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
 * Data class representing the settings for sampling during text generation.
 *
 * @property temp The temperature for sampling. Controls the randomness of the output.
 *                Higher values (e.g., 1.0) make the output more random, while lower values (e.g., 0.2) make it more deterministic.
 *                Default is 0.8f.
 * @property topP The nucleus sampling probability. It considers only the tokens whose cumulative probability mass exceeds this threshold.
 *                A common value is 0.9. Setting it to 1.0 disables nucleus sampling.
 *                Default is 0.95f.
 * @property minP The minimum probability for a token to be considered. Tokens with probabilities below this threshold are excluded.
 *                Helps to filter out very unlikely tokens.
 *                Default is 0.05f.
 * @property topK The number of top-k most probable tokens to consider for sampling.
 *                If set to a positive integer, only the `topK` most likely tokens are considered.
 *                Setting it to 0 disables top-k sampling.
 *                Default is 40.
 */
data class SamplingSettings(
    val temp: Float = 0.8f,
    val topP: Float = 0.95f,
    val minP: Float = 0.05f,
    val topK: Int = 40
)