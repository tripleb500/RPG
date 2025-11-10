package com.example.rpg.ui.parent.quest

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestDialog(
    quest: Quest,
    onSave: (Quest) -> Unit,
    onDismiss: () -> Unit,
    onOpen: (() -> Unit)? = null
) {
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_quest)) },
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
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = deadline?.let { SimpleDateFormat("MMM dd yyyy").format(it) } ?: "Select Deadline"
                    )
                }
                Button(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = deadline != null
                ) {
                    Text(
                        text = deadline?.let { SimpleDateFormat("h:mm a").format(it) } ?: "Select Time"
                    )
                }
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
}