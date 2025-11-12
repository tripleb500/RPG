package com.example.rpg.ui.parent.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status

@Composable
fun PendingQuestDialog(
    quest: Quest,
    onApprove: () -> Unit,
    onReassign: (Quest) -> Unit,
    onReject: (Quest) -> Unit,
    onDismiss: () -> Unit
) {
    var showUpdateDeadline by remember { mutableStateOf(false) }

    if (showUpdateDeadline) {
        UpdateDeadlineDialog(
            quest = quest,
            onSave = { updatedQuest ->
                // Mark as INPROGRESS and update deadline
                onReassign(updatedQuest.copy(status = Status.INPROGRESS))
                showUpdateDeadline = false
                onDismiss() // close PendingQuestDialog
            },
            onDismiss = { showUpdateDeadline = false }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFFF9C4),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.verify_quest),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Message text
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.what_would_you_like_to_do_with))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append(quest.title)
                        }
                        append(stringResource(R.string.quote_question))
                    },
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Buttons
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Row 1 button: Reassign
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { showUpdateDeadline = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Reassign")
                        }
                    }
                }

                // Row 2 buttons: Reject, Approve
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onReject(quest)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reject", textAlign = TextAlign.Center)
                    }

                    Button(
                        onClick = {
                            onApprove()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Approve", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}