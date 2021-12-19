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
import com.stream_suite.chat.ui.components.*

@Composable
fun CreateAccount(emailState: EmailState, onClickCreateAccount: (String, String, String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        Email(emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        val usernameState = remember { TextFieldState() }
        Username(
            usernameState = usernameState,
        )

        Spacer(modifier = Modifier.height(16.dp))

        val passwordState = remember { PasswordState() }
        Password(
            label = "New password",
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val confirmPasswordState = remember { ConfirmPasswordState(passwordState) }
        Password(
            label = "Confirm password",
            passwordState = confirmPasswordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = {
                onClickCreateAccount(
                    emailState.text,
                    confirmPasswordState.text,
                    usernameState.text
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onClickCreateAccount(
                    emailState.text,
                    confirmPasswordState.text,
                    usernameState.text
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid && usernameState.isValid && passwordState.isValid && confirmPasswordState.isValid
        ) {
            Text(
                text = "Create account"
            )
        }
    }
}