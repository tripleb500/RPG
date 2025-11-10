package com.example.rpg.ui.parent.quest

import android.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.rpg.data.model.Quest

@Composable
fun AvailableQuestDialog(
    quest: Quest,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Make Quest Available to all Children?")},
        text = {
            Text("Do you want to post '${quest.title}' to the family quest board so any child can accept it?")

        },
        confirmButton = {
            Button(onClick = {onApprove(); onDismiss() }) {
                Text("Approve")
            }
        },
        dismissButton = {
            Button(onClick = {onReject; onDismiss() }) {
                Text("Cancel")
            }
        }
    )

}