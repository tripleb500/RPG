package com.example.rpg.ui.parent.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun IncompleteQuestDialog(
    quest: Quest,
    onCancel: () -> Unit,
    onReassign: (Quest) -> Unit,
    onDelete: (Quest) -> Unit
) {
    var showUpdateDeadline by remember { mutableStateOf(false) }
    var showConfirmDelete by remember { mutableStateOf(false) }

    if (showUpdateDeadline) {
        UpdateDeadlineDialog(
            quest = quest,
            onSave = { updatedQuest ->
                // Mark as INPROGRESS and update deadline
                onReassign(updatedQuest.copy(status = Status.INPROGRESS))
                showUpdateDeadline = false
                onCancel() // close PendingQuestDialog
            },
            onDismiss = { showUpdateDeadline = false }
        )
    }

    Dialog(onDismissRequest = onCancel) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFFCDD2),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Reassign?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                CompositionLocalProvider(LocalContentColor provides Color.Black) {
                    // Quest Information
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append("Title: ")
                            }
                            append(quest.title)
                        })
                        Text(buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append("Description: ")
                            }
                            append(quest.description)
                        })
                        val formattedDate = quest.assignDate?.let {
                            SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()).format(it)
                        } ?: "N/A"
                        Text(buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append("Assigned On: ")
                            }
                            append(formattedDate)
                        })
                        val formattedDeadline = quest.deadlineDate?.let {
                            SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()).format(it)
                        } ?: "N/A"
                        Text(buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append("Deadline: ")
                            }
                            append(formattedDeadline)
                        })
                    }
                }

                // Buttons
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // First row: Cancel | Reassign
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onCancel,
                            modifier = Modifier.weight(1f)
                        ) { Text("Cancel", textAlign = TextAlign.Center) }

                        Button(
                            onClick = { showUpdateDeadline = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                        ) { Text("Reassign", textAlign = TextAlign.Center) }
                    }

                    // Second row: Delete full width
                    Button(
                        onClick = { showConfirmDelete = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Delete", color = Color.White, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
    if (showConfirmDelete) {
        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this quest?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(quest)
                        showConfirmDelete = false
                        onCancel() // also close the edit dialog
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                ) { Text("Delete") }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmDelete = false }
                ) { Text("Cancel") }
            }
        )
    }
}