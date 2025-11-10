package com.example.rpg.ui.parent.quest

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.rpg.data.model.Quest
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.parent.home.ParentHomeScreenViewModel

@Composable
fun CompletedQuestDialog(
    quest: Quest,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: ParentHomeScreenViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("COMPLETED Quest Completion?") },
        text = {
            Text("Mark '${quest.title}' as completed, or send it back to the child?")
        },
        confirmButton = {
            Button(onClick = { onApprove(); onDismiss() }) {
                Text("Approve")
            }
        },
        dismissButton = {
            Button(onClick = { onReject(); onDismiss() }) {
                Text("Reject")
            }
        }
    )
}