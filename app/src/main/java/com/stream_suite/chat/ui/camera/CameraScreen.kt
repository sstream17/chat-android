package com.stream_suite.chat.ui.camera

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.guava.await
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Camera(onClickCapture: (String) -> Unit = {}) {
    val imageCapture = remember {
        ImageCapture.Builder()
            .build()
    }

    val (cameraSelector, setCameraSelector) = remember {
        mutableStateOf(CameraSelector.DEFAULT_FRONT_CAMERA)
    }

    Scaffold {
        Column(
            Modifier.fillMaxSize()
        ) {
            CameraPreview(
                Modifier.fillMaxSize(),
                cameraSelector = cameraSelector,
                setCameraSelector = setCameraSelector,
                imageCapture = imageCapture
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShutterButton(
                cameraSelector = cameraSelector,
                imageCapture = imageCapture,
                onClickCapture = onClickCapture
            )
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
    setCameraSelector: (CameraSelector) -> Unit,
    implementationMode: PreviewView.ImplementationMode = PreviewView.ImplementationMode.COMPATIBLE,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    imageAnalysis: ImageAnalysis? = null,
    imageCapture: ImageCapture? = null,
    preview: Preview = remember { Preview.Builder().build() },
    enableTorch: Boolean = false,
    focusOnTap: Boolean = true
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProvider by produceState<ProcessCameraProvider?>(initialValue = null) {
        value = ProcessCameraProvider.getInstance(context).await()
    }

    val camera = remember(cameraProvider) {
        cameraProvider?.let {
            it.unbindAll()
            it.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                *listOfNotNull(imageAnalysis, imageCapture, preview).toTypedArray()
            )
        }
    }

    LaunchedEffect(camera, enableTorch) {
        camera?.let {
            if (it.cameraInfo.hasFlashUnit()) {
                it.cameraControl.enableTorch(enableTorch).await()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
        }
    }

    AndroidView(
        modifier = modifier.pointerInput(
            camera,
            focusOnTap,
            cameraSelector,
            cameraProvider,
            lifecycleOwner,
            imageAnalysis,
            imageCapture,
            preview
        ) {
            if (!focusOnTap) return@pointerInput

            detectTapGestures(
                onTap = {
                    val meteringPointFactory = SurfaceOrientedMeteringPointFactory(
                        size.width.toFloat(),
                        size.height.toFloat()
                    )

                    val meteringAction = FocusMeteringAction.Builder(
                        meteringPointFactory.createPoint(it.x, it.y),
                        FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE
                    ).build()

                    camera?.cameraControl?.startFocusAndMetering(meteringAction)
                },
                onDoubleTap = {
                    val newCameraSelector = when (cameraSelector) {
                        CameraSelector.DEFAULT_FRONT_CAMERA -> CameraSelector.DEFAULT_BACK_CAMERA
                        else -> CameraSelector.DEFAULT_FRONT_CAMERA
                    }
                    cameraProvider?.unbindAll()
                    cameraProvider?.bindToLifecycle(
                        lifecycleOwner,
                        newCameraSelector,
                        *listOfNotNull(imageAnalysis, imageCapture, preview).toTypedArray()
                    )
                    setCameraSelector(newCameraSelector)
                }
            )
        },
        factory = { _ ->
            PreviewView(context).also {
                it.scaleType = scaleType
                it.implementationMode = implementationMode
                preview.setSurfaceProvider(it.surfaceProvider)
            }
        }
    )
}

@Composable
fun ShutterButton(
    cameraSelector: CameraSelector,
    imageCapture: ImageCapture?,
    onClickCapture: (String) -> Unit
) {
    val context = LocalContext.current

    Button(onClick = { takePhoto(context, cameraSelector, imageCapture, onClickCapture) }) {
        Text("Capture")
    }
}

private fun takePhoto(
    context: Context,
    cameraSelector: CameraSelector,
    imageCapture: ImageCapture?,
    onClickCapture: (String) -> Unit
) {
    // Get a stable reference of the modifiable image capture use case
    val _imageCapture = imageCapture ?: return

    // Create time-stamped output file to hold the image
    val photoFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        SimpleDateFormat(
            "yyyyMMdd", Locale.US
        ).format(System.currentTimeMillis()) + ".jpg"
    )

    // Create output options object which contains file + metadata
    val metadata = ImageCapture.Metadata()
    if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
        metadata.isReversedHorizontal = true
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(photoFile)
        .setMetadata(metadata)
        .build()

    // Set up image capture listener, which is triggered after photo has
    // been taken
    _imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e("CAMERA", "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $savedUri"
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                Log.d("CAMERA", msg)
                val uriString = savedUri.toString()
                val uriEncoded = Base64.encodeToString(uriString.toByteArray(), Base64.DEFAULT)
                onClickCapture(uriEncoded)
            }
        })
}