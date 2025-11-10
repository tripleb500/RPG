package com.example.rpg.ui.parent.quest

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.rpg.data.model.Quest

@Composable
fun PendingQuestDialog(
    quest: Quest,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Approve Quest Completion?") },
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