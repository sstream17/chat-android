package com.stream_suite.chat.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.stream_suite.chat.ui.components.Email
import com.stream_suite.chat.ui.components.EmailState

@Composable
fun LoginLanding(
    emailState: EmailState,
    onClickSignIn: () -> Unit = {},
    onClickCreateAccount: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        Email(emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClickSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid
        ) {
            Text(
                text = "Sign in"
            )
        }

        Surface {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "or",
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.paddingFromBaseline(top = 24.dp)
                )
            }
        }

        OutlinedButton(
            onClick = onClickCreateAccount,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid
        ) {
            Text(
                text = "Create account"
            )
        }
    }
}