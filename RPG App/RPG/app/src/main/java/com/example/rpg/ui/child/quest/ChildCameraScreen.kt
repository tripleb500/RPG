package com.example.rpg.ui.child.quest

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.rpg.ui.child.home.ChildHomeScreenViewModel
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ChildCameraScreen(
    viewModel: ChildHomeScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onPhotoTaken: (Bitmap) -> Unit
) {
    var context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // FIX: Properly constrained Scaffold
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Black background for full screen
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Take Photo") },
                icon = { Icon(Icons.Default.PhotoCamera, "Take photo") },
                onClick = {
                    val mainExecutor = ContextCompat.getMainExecutor(context)
                    cameraController.takePicture(
                        mainExecutor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                try {
                                    val bitmap = image.toBitmap()

                                    val portraitBitmap = ensurePortrait(bitmap)
                                    Toast.makeText(context, "Photo captured!", Toast.LENGTH_SHORT).show()
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        onPhotoTaken(portraitBitmap)
                                    }, 1000)
                                } finally {
                                    image.close()
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                },
            )
        },
        containerColor = Color.Black // Make scaffold background black
    ) { paddingValues: PaddingValues ->
        // FIX: Use Box to ensure full screen coverage
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        scaleType = PreviewView.ScaleType.FILL_CENTER // Changed to FILL_CENTER
                        controller = cameraController
                    }
                }
            )
        }
    }

    // Handle permission and camera binding
    LaunchedEffect(cameraPermissionState.status) {
        if (cameraPermissionState.status == PermissionStatus.Granted) {
            cameraController.bindToLifecycle(lifecycleOwner)
        }
    }
}

fun ensurePortrait(bitmap: Bitmap): Bitmap {
    return if (bitmap.width > bitmap.height) {
        // Landscape - rotate to portrait
        val matrix = Matrix()
        matrix.postRotate(90f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        bitmap
    }
}
