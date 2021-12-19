package com.stream_suite.chat.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.stream_suite.chat.ui.components.Email
import com.stream_suite.chat.ui.components.EmailState
import com.stream_suite.chat.ui.components.Password
import com.stream_suite.chat.ui.components.TextFieldState

@Composable
fun SignIn(emailState: EmailState, onClickSignIn: (String, String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        Email(emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        // No password input validation needed when signing in, use base TextFieldState
        val passwordState = remember { TextFieldState() }
        Password(
            label = "Password",
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = { onClickSignIn(emailState.text, passwordState.text) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onClickSignIn(emailState.text, passwordState.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid
        ) {
            Text(
                text = "Sign in"
            )
        }
    }
}