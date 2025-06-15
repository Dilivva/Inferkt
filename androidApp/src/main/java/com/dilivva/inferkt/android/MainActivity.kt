package com.dilivva.inferkt.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dilivva.inferkt.GenerationEvent
import com.dilivva.inferkt.ModelSettings
import com.dilivva.inferkt.SamplingSettings
import com.dilivva.inferkt.createInference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class MainActivity : ComponentActivity() {

    init {
        //System.loadLibrary("infer-android")
        CpuUtils.loadLibrary()
        //System.loadLibrary("inferkt")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Column (
                    modifier = Modifier.fillMaxSize(),
                ) {
                    GreetingView()
                }
            }
        }
    }
}

@Composable
fun GreetingView() {
    val context = LocalContext.current
    val path = remember {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let { parent ->
            File(parent, "inference").apply { mkdirs() }
        }
        println("Location: ${mediaDir?.absolutePath}")

        File(mediaDir, "model.gguf").absolutePath
    }
    val inference = remember { createInference() }
    val scope = rememberCoroutineScope()
    var chatId by remember {
        mutableIntStateOf(-1)
    }
    var messages by remember {
        mutableStateOf(listOf<ChatMessage>())
    }
    var userMessage by remember {
        mutableStateOf("")
    }
    var isLoaded by remember {
        mutableStateOf(false)
    }


    var prompt by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        inference.setSamplingParams(SamplingSettings())
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)){
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 10.dp, vertical = 20.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(messages, key = { it.id }) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        when (it.type) {
                            ChatMessage.Type.User -> {
                                Text(
                                    text = it.message.value,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.End).fillMaxWidth(0.5f)
                                        .padding(start = 5.dp)
                                        .background(Color.Blue, RoundedCornerShape(8.dp))
                                        .padding(5.dp)
                                )
                            }

                            ChatMessage.Type.Bot -> {
                                Text(
                                    text = it.message.value,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.Start).fillMaxWidth(0.5f)
                                        .padding(end = 5.dp)
                                        .background(Color.Red, RoundedCornerShape(8.dp))
                                        .padding(5.dp)
                                )
                            }
                        }
                    }
                }

            }
        }
        Column(modifier = Modifier) {
            TextField(
                userMessage,
                onValueChange = { userMessage = it },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                onClick = {
                    scope.launch(Dispatchers.IO){
                        //println("Loading model")
                        val modelSettings = ModelSettings(
                            modelPath = path,
                            numberOfGpuLayers = 99,
                            numberOfThreads = 0
                        )
                        isLoaded = inference.preloadModel(modelSettings){
                            println("Progress: $it")
                            true
                        }
                    }

                }
            ) {
                Text("Load model")
            }
            Button(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                enabled = isLoaded,
                onClick = {
                    scope.launch(Dispatchers.IO){
                        chatId++
                        prompt = userMessage
                        userMessage = ""
                        messages = messages + ChatMessage(message = mutableStateOf(prompt), type = ChatMessage.Type.User, id = chatId)
                        chatId++
                        val botmessage = ChatMessage(message = mutableStateOf("Loading..."), type = ChatMessage.Type.Bot, id = chatId)
                        messages = messages + botmessage
                        println("Sending: $chatId")
                       inference.chat(prompt, 500){
                           when(it){
                               is GenerationEvent.Error -> println("Error: ${it.error.name}")
                               GenerationEvent.Generated -> println("Generated")
                               is GenerationEvent.Generating -> {
                                   botmessage.message.value += it.text
                               }
                               GenerationEvent.Loading -> println("Loading")
                           }

                        }
                    }
                }
            ) {
                Text("Send")
            }
        }
    }



    Button(
        modifier = Modifier.wrapContentSize(),
        onClick = {
            scope.launch(Dispatchers.IO){
                println("Loading model")
                val modelSettings = ModelSettings(
                    modelPath = path
                )
                isLoaded = inference.preloadModel(modelSettings){
                    println("Progress: $it")
                    true
                }
            }

    }) {
        Text("Load model")
    }
}


data class ChatMessage(
    val message: MutableState<String>,
    val type: Type,
    val id: Int
){
    enum class Type{
        User, Bot
    }
}

@Preview
@Composable
fun DefaultPreview() {

}
