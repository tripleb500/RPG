package com.example.rpg.ui.child.quest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.ui.child.home.ChildHomeScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QuestDialog(
    quest: Quest,
    viewModel: ChildHomeScreenViewModel,
    onDismissRequest: () -> Unit,
    onCompleteClicked: (Quest) -> Unit
) {
    val assigneeName by viewModel.getQuestParentName(quest.assignee)

    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    var hasPermission = cameraPermissionState.status.isGranted
    val onRequestPermission = cameraPermissionState::launchPermissionRequest
    var openCamera by remember { mutableStateOf(false) }

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
                // Title
                Text(
                    text = quest.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Reward
                Text(stringResource(R.string.reward_label, quest.rewardType))

                // Amount
                Text(stringResource(R.string.amount_label, quest.rewardAmount))

                // Due Date
                Text(stringResource(R.string.due_date_label, quest.deadlineDate ?: "No deadline"))

                // Assignee
                Text(
                    stringResource(
                        R.string.assigned_by_label,
                        assigneeName ?: stringResource(R.string.loading)
                    )
                )

                Spacer(Modifier.height(16.dp))

                LaunchedEffect(openCamera, hasPermission) {
                    if (openCamera) {
                        println("Im in the open camera if statement")
                        if (!hasPermission) {
                            println("Im in the has permission request")
                            onRequestPermission()
                            // Wait for permission result before proceeding
                        }
                    }
                }

                // Complete button
                Button(
                    onClick = {
                        println("Complete button clicked for quest: ${quest.title}")
                        //onCompleteClicked(quest)
                        openCamera = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.turn_in_quest))
                }

                if (openCamera && hasPermission) {
                    Dialog(
                        onDismissRequest = { openCamera = false },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        TakeQuestPictureScreen(
                            onClose = {
                                openCamera = false
                                onDismissRequest() // Dismiss original dialog after camera closes
                            }
                        )
                    }
                }

                // Cancel
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancel", color = Color.Red)
                }
            }
        }
    }
}
