package com.stream_suite.chat.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stream_suite.chat.ui.components.EmailState
import com.stream_suite.chat.ui.login.LoginScreens.*
import com.stream_suite.chat.ui.theme.ChatTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Login()
        }
    }
}

@Composable
fun Login() {
    ChatTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            Scaffold(modifier = Modifier.padding(16.dp)) { innerPadding ->
                LoginNavHost(navController, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun LoginNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val emailState = remember { EmailState() }

    NavHost(
        navController = navController,
        startDestination = Landing.name,
        modifier = modifier
    ) {
        composable(Landing.name) {
            LoginLanding(
                emailState = emailState,
                onClickSignIn = { navController.navigate(SignIn.name) },
                onClickCreateAccount = { navController.navigate(CreateAccount.name) }
            )
        }
        composable(CreateAccount.name) {
            CreateAccount()
        }
        composable(SignIn.name) {
            SignIn(
                emailState = emailState
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login()
}