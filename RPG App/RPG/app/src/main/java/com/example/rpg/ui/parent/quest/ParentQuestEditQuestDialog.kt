package com.example.rpg.ui.parent.quest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.rpg.data.model.Quest

@Composable
fun EditQuestDialog(
    quest: Quest,
    onSave: (Quest) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(quest.title) }
    var description by remember { mutableStateOf(quest.description ?: "") }
    var deadline by remember { mutableStateOf(quest.deadlineDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Quest") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                // You can use a date picker here
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(quest.copy(title = title, description = description, deadlineDate = deadline))
                onDismiss()
            }) { Text("Save") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
