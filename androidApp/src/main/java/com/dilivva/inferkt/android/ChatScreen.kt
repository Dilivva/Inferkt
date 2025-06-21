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

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatScreen() {
    val context = LocalContext.current
    val viewModel = viewModel<ChatViewModel>()
    val modelPicker = rememberModelPicker {
        viewModel.setModelPath(context, it)
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier.align(Alignment.BottomStart)
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(viewModel.messages, key = { it.id }) {
                    ChatItem(it)
                }

            }
        }
        if (viewModel.isLoaded){
            ChatControl(viewModel)
        }else{
            if (viewModel.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }else {
                ModelControl {
                    modelPicker.launch(arrayOf("*/*"))
                }
            }
        }

    }

}

@Composable
private fun ChatItem(chatMessage: ChatMessage){
    Column(modifier = Modifier.fillMaxWidth()) {
        when (chatMessage.type) {
            ChatMessage.Type.User -> {
                Text(
                    text = chatMessage.message.value,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.End)
                        .background(Color.Blue, RoundedCornerShape(16.dp))
                        .padding(8.dp)
                )
            }

            ChatMessage.Type.Bot -> {
                Text(
                    text = chatMessage.message.value,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Start)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun ModelControl(onClick: () -> Unit){
    Button(
        modifier = Modifier.fillMaxWidth().height(40.dp),
        onClick = onClick
    ) {
        Text("Pick a model")
    }
}

@Composable
private fun ChatControl(viewModel: ChatViewModel){
    Row {
        TextField(
            viewModel.userMessage,
            onValueChange = { viewModel.userMessage = it },
            modifier = Modifier.weight(1f)
        )
        Button(
            modifier = Modifier.padding(5.dp),
            onClick = {
                if (viewModel.isGenerating) viewModel.cancelGeneration()
                else viewModel.sendMessage()
            }
        ) {
            Text(if (viewModel.isGenerating) "Stop" else "Send")
        }
    }
}

@Composable
private fun rememberModelPicker(onPicked: (Uri) -> Unit): ManagedActivityResultLauncher<Array<String>, Uri?> {
   return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { it?.let(onPicked) }
}