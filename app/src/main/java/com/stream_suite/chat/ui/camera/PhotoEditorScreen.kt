package com.stream_suite.chat.ui.camera

import android.util.Base64
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun PhotoEditor(uriEncoded: String) {
    val uri = String(Base64.decode(uriEncoded, Base64.DEFAULT))

    Scaffold {
        Column(Modifier.fillMaxSize()) {
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
    }
}