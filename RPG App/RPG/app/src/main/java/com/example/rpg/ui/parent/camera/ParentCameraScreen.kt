package com.example.rpg.ui.parent.camera

import android.content.ContentValues
import android.content.Context

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.rpg.ui.parent.addquest.ParentAddQuestViewModel
import java.io.File

@Composable
fun ParentCameraScreen(
    controller: LifecycleCameraController,
    overlayNavController: NavHostController,
    modifier: Modifier = Modifier,
    parentViewModel: ParentAddQuestViewModel = hiltViewModel()
) {
    System.out.println("Currently in ParentCameraScreen")
    val lifecycleOwner = LocalLifecycleOwner.current
    val context: Context = LocalContext.current

    Box(){
        AndroidView(
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            },
            modifier = modifier.fillMaxSize()
        )
        IconButton(
            onClick = {
                takePhoto(
                    controller = controller,
                    context,
                    onPhotoTaken = { uri ->
                        overlayNavController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("photoUri", uri)
                        overlayNavController.popBackStack()
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = "Take photo"
            )
        }
        IconButton(
            onClick = {
                overlayNavController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Exit"
            )
        }
    }
}

private fun takePhoto(
    controller: LifecycleCameraController,
    context: Context,
    onPhotoTaken: (Uri?) -> Unit
) {
    // 1. Create a file to save the photo to
    val photoFile = File(
        context.externalCacheDir,
        "${System.currentTimeMillis()}.jpg"
    )

    // 2. Get a content URI for that file
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    // 3. Take the picture and write it directly to the file
    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, photoFile.name)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp")
                    }
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        photoFile.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    onPhotoTaken(uri)
                } else {
                    onPhotoTaken(null)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "Photo capture failed: ${exception.message}", exception)
            }
        }
    )
}


@Composable
private fun LastPhotoPreview(
    modifier: Modifier = Modifier,
    lastCapturedPhoto: Uri
) {
    Card(
        modifier = modifier
            .size(128.dp)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        AsyncImage(
            model = lastCapturedPhoto,
            contentDescription = "Last captured photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
