package com.stream_suite.chat.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.stream_suite.chat.MainActivity
import com.stream_suite.chat.ui.components.EmailState
import com.stream_suite.chat.ui.login.LoginScreens.*
import com.stream_suite.chat.ui.theme.ChatTheme

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Login(
                onClickSignIn = { email, password ->
                    signIn(email, password)
                },
                onClickCreateAccount = { email, password, username
                    ->
                    createAccount(email, password, username)
                }
            )
        }

        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            returnToMainActivity()
        }
    }

    private fun returnToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        return
    }

    private fun createAccount(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
                    }
                    user!!.updateProfile(profileUpdates)
                    returnToMainActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    returnToMainActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}

@Composable
fun Login(
    onClickSignIn: (String, String) -> Unit,
    onClickCreateAccount: (String, String, String) -> Unit
) {
    ChatTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            Scaffold(modifier = Modifier.padding(16.dp)) { innerPadding ->
                LoginNavHost(
                    navController,
                    onClickSignIn,
                    onClickCreateAccount,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun LoginNavHost(
    navController: NavHostController,
    onClickSignIn: (String, String) -> Unit,
    onClickCreateAccount: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
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
            CreateAccount(
                emailState = emailState,
                onClickCreateAccount = onClickCreateAccount
            )
        }
        composable(SignIn.name) {
            SignIn(
                emailState = emailState,
                onClickSignIn = onClickSignIn
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(onClickSignIn = { _, _ -> }, onClickCreateAccount = { _, _, _ -> })
}