package com.example.rpg.ui.parent.quest

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.rpg.data.model.Quest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestDialog(
    quest: Quest,
    onSave: (Quest) -> Unit,
    onDelete: (Quest) -> Unit,
    onDismiss: () -> Unit,
    onOpen: (() -> Unit)? = null
) {
    var showConfirmDelete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onOpen?.invoke()
    }

    var title by remember { mutableStateOf(quest.title) }
    var description by remember { mutableStateOf(quest.description) }
    var deadline by remember { mutableStateOf(quest.deadlineDate) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Get current date/time for initializing picker
    val calendar = Calendar.getInstance().apply {
        time = deadline ?: Date()
    }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
                .fillMaxWidth(0.95f)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Edit Quest",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                )

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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Button(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = deadline?.let { SimpleDateFormat("MMM dd yyyy").format(it) }
                                    ?: "Select Deadline"
                            )
                        }

                        Button(
                            onClick = { showTimePicker = true },
                            enabled = deadline != null,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = deadline?.let { SimpleDateFormat("h:mm a").format(it) }
                                    ?: "Select Time"
                            )
                        }
                    }
                }
                Button(
                    onClick = { showConfirmDelete = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete", color = Color.White)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            onSave(
                                quest.copy(
                                    title = title,
                                    description = description,
                                    deadlineDate = deadline
                                )
                            )
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("Save") }
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }.time
                deadline = selectedDate
                showDatePicker = false
            },
            year, month, day
        ).apply {
            setOnDismissListener { showDatePicker = false }
            show()
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val cal = Calendar.getInstance().apply {
                    deadline?.let { time = it }
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                }
                deadline = cal.time
                showTimePicker = false
            },
            hour, minute,
            DateFormat.is24HourFormat(context)
        ).apply { setOnDismissListener { showTimePicker = false }; show() }
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
                        onDismiss() // also close the edit dialog
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