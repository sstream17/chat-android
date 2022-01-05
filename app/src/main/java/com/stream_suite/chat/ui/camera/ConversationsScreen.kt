package com.stream_suite.chat.ui.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.stream_suite.chat.data.entity.Conversation

@Composable
fun Conversations(viewModel: ConversationsViewModel) {
    val conversations = viewModel.allConversations.observeAsState()
    Scaffold {
        if (conversations.value?.isNotEmpty() == true) {
            LazyColumn {
                items(conversations.value!!) { conversation ->
                    Text(conversation.name)
                }
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Button(onClick = {
                viewModel.insert(Conversation("Spencer"))
            }) {
                Text("Start chat")
            }
        }
    }
}