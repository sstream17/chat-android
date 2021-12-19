package com.stream_suite.chat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.stream_suite.chat.ui.camera.Camera
import com.stream_suite.chat.ui.camera.MainScreens.Camera
import com.stream_suite.chat.ui.camera.MainScreens.PhotoEditor
import com.stream_suite.chat.ui.camera.PhotoEditor
import com.stream_suite.chat.ui.login.LoginActivity
import com.stream_suite.chat.ui.theme.ChatTheme

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // When running in debug mode, connect to the Firebase Emulator Suite
        // "10.0.2.2" is a special value which allows the Android emulator to
        // connect to "localhost" on the host computer. The port values are
        // defined in the firebase.json file.
        if (BuildConfig.DEBUG) {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
        }

        setContent {
            MainView { signOut() }
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        auth = Firebase.auth
        if (auth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

@Composable
fun MainView(signOut: () -> Unit) {
    ChatTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            Scaffold {
                MainNavHost(navController)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Spencer Chat", fontSize = 30.sp)
                    Button(onClick = signOut) {
                        Text("Sign out")
                    }
                }
            }
        }
    }
}

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Camera.name,
        modifier = modifier
    ) {
        composable(Camera.name) {
            Camera { uri ->
                navigateToPhotoEditorWithUri(navController, uri)
            }
        }
        val photoEditorName = PhotoEditor.name
        composable(
            route = "$photoEditorName/{uri}",
            arguments = listOf(
                navArgument("uri") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val uriEncoded = entry.arguments?.getString("uri")
            if (!uriEncoded.isNullOrEmpty()) {
                PhotoEditor(uriEncoded = uriEncoded)
            }
        }
    }
}

private fun navigateToPhotoEditorWithUri(navController: NavHostController, uriEncoded: String) {
    navController.navigate("${PhotoEditor.name}/$uriEncoded")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainView {}
}