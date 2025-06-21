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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

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
                ChatScreen()
            }
        }
    }
}

//@Composable
//fun GreetingView() {
////    val context = LocalContext.current
////    val path = remember {
////        val mediaDir = context.externalMediaDirs.firstOrNull()?.let { parent ->
////            File(parent, "inference").apply { mkdirs() }
////        }
////        println("Location: ${mediaDir?.absolutePath}")
////
////        File(mediaDir, "model.gguf").absolutePath
////    }
//
//    val scope = rememberCoroutineScope()
//    var chatId by remember {
//        mutableIntStateOf(-1)
//    }
//    var isLoaded by remember {
//        mutableStateOf(false)
//    }
//
//
//    var prompt by remember {
//        mutableStateOf("")
//    }
//
//    LaunchedEffect(Unit) {
//        inference.setSamplingParams(SamplingSettings())
//    }
//
//    Column(modifier = Modifier.fillMaxSize().background(Color.White)){
//        Box(modifier = Modifier.weight(1f)) {
//            LazyColumn(
//                state = rememberLazyListState(),
//                modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 10.dp, vertical = 20.dp),
//                contentPadding = PaddingValues(vertical = 10.dp),
//                verticalArrangement = Arrangement.spacedBy(5.dp)
//            ) {
//                items(messages, key = { it.id }) {
//                    Column(modifier = Modifier.fillMaxWidth()) {
//                        when (it.type) {
//                            ChatMessage.Type.User -> {
//                                Text(
//                                    text = it.message.value,
//                                    color = Color.White,
//                                    modifier = Modifier.align(Alignment.End).fillMaxWidth(0.5f)
//                                        .padding(start = 5.dp)
//                                        .background(Color.Blue, RoundedCornerShape(8.dp))
//                                        .padding(5.dp)
//                                )
//                            }
//
//                            ChatMessage.Type.Bot -> {
//                                Text(
//                                    text = it.message.value,
//                                    color = Color.White,
//                                    modifier = Modifier.align(Alignment.Start).fillMaxWidth(0.5f)
//                                        .padding(end = 5.dp)
//                                        .background(Color.Red, RoundedCornerShape(8.dp))
//                                        .padding(5.dp)
//                                )
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//        Column(modifier = Modifier) {
//            TextField(
//                userMessage,
//                onValueChange = { userMessage = it },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Button(
//                modifier = Modifier.fillMaxWidth().height(40.dp),
//                onClick = {
//                    scope.launch(Dispatchers.IO){
//                        //println("Loading model")
//
//                    }
//
//                }
//            ) {
//                Text("Load model")
//            }
//            Button(
//                modifier = Modifier.fillMaxWidth().height(40.dp),
//                enabled = isLoaded,
//                onClick = {
////                    scope.launch(Dispatchers.IO){
////                        chatId++
////                        prompt = userMessage
////                        userMessage = ""
////                        messages = messages + ChatMessage(message = mutableStateOf(prompt), type = ChatMessage.Type.User, id = chatId)
////                        chatId++
////                        val botmessage = ChatMessage(message = mutableStateOf("Loading..."), type = ChatMessage.Type.Bot, id = chatId)
////                        messages = messages + botmessage
////                        println("Sending: $chatId")
////                       inference.chat(prompt, 500){
////                           when(it){
////                               is GenerationEvent.Error -> println("Error: ${it.error.name}")
////                               GenerationEvent.Generated -> println("Generated")
////                               is GenerationEvent.Generating -> {
////                                   botmessage.message.value += it.text
////                               }
////                               GenerationEvent.Loading -> println("Loading")
////                           }
////
////                        }
////                    }
//                }
//            ) {
//                Text("Send")
//            }
//        }
//    }
//
//
//
////    Button(
////        modifier = Modifier.wrapContentSize(),
////        onClick = {
////            scope.launch(Dispatchers.IO){
////                println("Loading model")
////                val modelSettings = ModelSettings(
////                    modelPath = path
////                )
////                isLoaded = inference.preloadModel(modelSettings){
////                    println("Progress: $it")
////                    true
////                }
////            }
////
////    }) {
////        Text("Load model")
////    }
//}
//
//
//
//@Preview
//@Composable
//fun DefaultPreview() {
//
//}
