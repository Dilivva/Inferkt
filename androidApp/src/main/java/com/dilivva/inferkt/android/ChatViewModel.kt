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

package com.dilivva.inferkt.android

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilivva.inferkt.GenerationEvent
import com.dilivva.inferkt.ModelSettings
import com.dilivva.inferkt.SamplingSettings
import com.dilivva.inferkt.createInference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatViewModel: ViewModel() {
    private val inference =  createInference()
    private var modelPath = ""
    var userMessage by mutableStateOf("")
    var isLoading by  mutableStateOf(false)
        private set
    var isLoaded by  mutableStateOf(false)
        private set
    var isGenerating by  mutableStateOf(false)
        private set
    var messages by mutableStateOf(listOf<ChatMessage>())
        private set

    private val dispatcher: CoroutineDispatcher = Executors.newSingleThreadExecutor {
        thread(start = false, name = "") {
            it.run()
        }.apply {
            uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, exception: Throwable ->
                println("Unhandled exception: ${exception.message}")
            }
        }
    }.asCoroutineDispatcher()

    fun setModelPath(context: Context, uri: Uri) {
        isLoading = true
        viewModelScope.launch(dispatcher) {
            val file = copyUriToInternalStorage(context, uri, "model.gguf") ?: return@launch
            println("File: ${file.absolutePath}")
            modelPath = file.absolutePath
            loadModel()
        }
    }

    private suspend fun loadModel() {
        val modelSettings = ModelSettings(
            modelPath = modelPath,
            numberOfGpuLayers = 99
        )
        val isLoaded = inference.preloadModel(modelSettings){ true }
        inference.setSamplingParams(SamplingSettings())
        withContext(Dispatchers.Main){
            this@ChatViewModel.isLoaded = isLoaded
            isLoading = false
        }

    }
    @OptIn(ExperimentalUuidApi::class)
    fun sendMessage() = viewModelScope.launch(Dispatchers.IO){
        if (!isLoaded) return@launch
        val prompt = userMessage
        userMessage = ""
        val userMessage = ChatMessage(
            message = mutableStateOf(prompt),
            type = ChatMessage.Type.User,
            id = Uuid.random().toString()
        )
        messages += userMessage
        val botMessage = ChatMessage(
            message = mutableStateOf("Loading...\n\n"),
            type = ChatMessage.Type.Bot,
            id = Uuid.random().toString()
        )
        messages += botMessage
        val botMessageIndex = messages.size - 1
        inference.chat(
            prompt = prompt,
            maxTokens = 1024,
            onGenerate = {
                when(it){
                    is GenerationEvent.Error -> println("Error: ${it.error}")
                    GenerationEvent.Generated -> { isGenerating = false }
                    is GenerationEvent.Generating -> {
                        //isGenerating = true
                        messages[botMessageIndex].message.value += it.text
                    }
                    GenerationEvent.Loading -> {
                        isGenerating = true
                    }
                }
            }
        )
    }

    fun cancelGeneration(){
        inference.cancelGeneration()
    }

    private suspend fun copyUriToInternalStorage(
        context: Context,
        sourceUri: Uri,
        destinationFileName: String
    ): File? = withContext(Dispatchers.IO) {
        val destinationFile = File(context.filesDir, destinationFileName)
        try {
            context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            destinationFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onCleared() {
        File(modelPath).delete()
        inference.release()
        super.onCleared()
    }

}