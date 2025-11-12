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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun UpdateDeadlineDialog(
    quest: Quest,
    onSave: (Quest) -> Unit,
    onDismiss: () -> Unit
) {
    var newDeadline by remember { mutableStateOf(quest.deadlineDate) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val calendar = Calendar.getInstance().apply {
        time = newDeadline ?: Date()
    }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFA8E6CF),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Update Deadline",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Message
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.update_the_deadline_for))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append(quest.title)
                        }
                        append(stringResource(R.string.so_it_can_be_reassigned))
                    },
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Date picker button
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = newDeadline?.let { SimpleDateFormat("dd MMM yyyy").format(it) }
                            ?: "Select Date"
                    )
                }

                // Time picker button
                Button(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = newDeadline != null
                ) {
                    Text(
                        text = newDeadline?.let { SimpleDateFormat("h:mm a").format(it) }
                            ?: "Select Time"
                    )
                }

                // Buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.cancel), textAlign = TextAlign.Center)
                    }

                    Button(
                        onClick = {
                            onSave(quest.copy(deadlineDate = newDeadline))
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.reassign), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val cal = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                    // Preserve existing time if already set
                    newDeadline?.let {
                        val tempCal = Calendar.getInstance().apply { time = it }
                        set(Calendar.HOUR_OF_DAY, tempCal.get(Calendar.HOUR_OF_DAY))
                        set(Calendar.MINUTE, tempCal.get(Calendar.MINUTE))
                    }
                }
                newDeadline = cal.time
                showDatePicker = false
            },
            year, month, day
        ).apply { setOnDismissListener { showDatePicker = false }; show() }
    }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val cal = Calendar.getInstance().apply {
                    newDeadline?.let { time = it }
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                }
                newDeadline = cal.time
                showTimePicker = false
            },
            hour, minute,
            DateFormat.is24HourFormat(context)
        ).apply { setOnDismissListener { showTimePicker = false }; show() }
    }
}