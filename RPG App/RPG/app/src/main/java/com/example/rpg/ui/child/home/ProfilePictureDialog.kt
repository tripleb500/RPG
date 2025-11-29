package com.example.rpg.ui.child.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.rpg.data.model.User
import com.example.rpg.ui.child.quest.ChildCameraScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfilePictureDialog(
    onDismissRequest: () -> Unit,
    viewModel: ChildHomeScreenViewModel = hiltViewModel(),
    user: User
) {


    val cameraPermissionState: PermissionState =
        rememberPermissionState(android.Manifest.permission.CAMERA)

    var hasPermission = cameraPermissionState.status.isGranted
    val onRequestPermission = cameraPermissionState::launchPermissionRequest

    var showCamera by remember { mutableStateOf(false) }

    val imageUrl by viewModel.submittedImage.collectAsState()

    LaunchedEffect(cameraPermissionState.status) {
        if (showCamera && cameraPermissionState.status.isGranted) {
            // Permission granted, camera will show
        } else if (showCamera && !cameraPermissionState.status.isGranted) {
            // Permission not granted, hide camera
            showCamera = false
        }
    }

    if (showCamera && hasPermission) {
        ChildCameraScreen(
            onPhotoTaken = { bitmap ->
                viewModel.uploadImageForQuest(user.id, bitmap)
                showCamera = false
            }
        )
        return
    }
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (imageUrl != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Profile Picture:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Profile Picture image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Button(
                        onClick = {
                            if (hasPermission) {
                                showCamera = true
                            } else {
                                onRequestPermission()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Retake Picture")
                    }

                }
                else {
                    Button(
                        onClick = {
                            if (hasPermission) {
                                showCamera = true
                            } else {
                                onRequestPermission()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Take Profile Picture")
                    }
                }
                Button(
                    onClick = {
                        viewModel.updateProfilePicture(user)
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Text("Save")
                }
            }

        }
    }
}
