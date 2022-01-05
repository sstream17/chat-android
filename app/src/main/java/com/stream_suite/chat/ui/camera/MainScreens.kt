package com.stream_suite.chat.ui.camera

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainScreens(
    val icon: ImageVector
) {
    Camera(
        icon = Icons.Filled.Phone
    ),
    Conversations(
        icon = Icons.Filled.Home
    ),
    PhotoEditor(
        icon = Icons.Filled.Info
    );

    companion object {
        fun fromRoute(route: String?): MainScreens =
            when (route?.substringBefore("/")) {
                Camera.name -> Camera
                null -> Camera
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}