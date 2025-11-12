package com.example.rpg.ui.child.quest

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.rpg.R
import com.example.rpg.data.model.Quest


@Composable
fun ChildAvailableQuestDialog(
    quest: Quest,
    onCancel: () -> Unit,
    onAccept: (Quest) -> Unit,
) {
    var showConfirmAccept by remember { mutableStateOf(false) }

    // Main custom Dialog
    Dialog(onDismissRequest = onCancel) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        )  {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Accept Quest?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = "Do you want to accept this quest?",
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", textAlign = TextAlign.Center)
                    }

                    Button(
                        onClick = { showConfirmAccept = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A))
                    ) {
                        Text("Accept", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }

    // Confirmation AlertDialog
    if (showConfirmAccept) {
        AlertDialog(
            onDismissRequest = { showConfirmAccept = false },
            title = { Text("Confirm Accept") },
            text = { Text("Are you sure you want to accept this quest?") },
            confirmButton = {
                Button(
                    onClick = {
                        onAccept(quest)
                        showConfirmAccept = false
                        onCancel() // close main dialog as well
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A))
                ) {
                    Text("Accept")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmAccept = false }
                ) { Text("Cancel") }
            }
        )
    }
}