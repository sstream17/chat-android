package com.stream_suite.chat.ui.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

enum class LoginScreens(
    val icon: ImageVector
) {
    Landing(
        icon = Icons.Filled.Home
    ),
    CreateAccount(
        icon = Icons.Filled.AccountCircle
    ),
    SignIn(
        icon = Icons.Filled.ShoppingCart
    );

    companion object {
        fun fromRoute(route: String?): LoginScreens =
            when (route?.substringBefore("/")) {
                CreateAccount.name -> CreateAccount
                SignIn.name -> SignIn
                Landing.name -> Landing
                null -> Landing
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}