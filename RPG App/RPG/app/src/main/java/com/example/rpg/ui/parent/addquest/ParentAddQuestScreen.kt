package com.example.rpg.ui.parent.addquest

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.rpg.ui.auth.AuthViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun ParentAddQuestScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ParentAddQuestViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    AddQuestContent(navController = overlayNavController)
}

@Composable
fun AddQuestContent(
    modifier: Modifier = Modifier,
    viewModel: ParentAddQuestViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val dueDate by viewModel.dueDate.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val quest by viewModel.quest.collectAsState()

    viewModel.fetchChildren()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Add Quest", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = quest.title,
            onValueChange = { viewModel.setQuestTitle(it) },
            label = { Text(text = "Title") },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = quest.description,
            onValueChange = { viewModel.setQuestDescription(it) },
            label = { Text(text = "Description") },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { showDatePicker = true }) {
            Text(text = dueDate?.let { SimpleDateFormat("dd MMM yyyy").format(it) }
                ?: "Select Due Date")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(onClick = { showTimePicker = true }, enabled = dueDate != null) {
            Text(text = dueDate?.let { SimpleDateFormat("h:mm a").format(it) }
                ?: "Select Time")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                viewModel.addQuest()
                navController.popBackStack()
            },
            enabled = dueDate != null && quest.title.isNotBlank() && quest.description.isNotBlank()
        ) {
            Text("Assign Quest")
        }

        val children by viewModel.children.collectAsState()
        val selectedChild by viewModel.selectedChild.collectAsState()

        //val children = listOf(User(firstname="Alice"), User(firstname="Bob"))
        //var selectedChild by remember { mutableStateOf<User?>(children.firstOrNull()) }

        LazyColumn {
            items(children) { child ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            viewModel.setChild(child)
                        },
                    colors = if (child == selectedChild) CardDefaults.cardColors(containerColor = Color.LightGray)
                    else CardDefaults.cardColors()
                ) {
                    Text(
                        text = child.firstname, modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        if (showDatePicker) {
            val dialog = DatePickerDialog(
                context, { _, selectedYear, selectedMonth, selectedDay ->
                    val selected = Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDay)
                    }.time
                    viewModel.setDeadlineDate(selected)
                    showDatePicker = false
                }, year, month, day
            )
            dialog.setOnDismissListener { showDatePicker = false }
            dialog.show()
        }

        if (showTimePicker) {
            val dialog = TimePickerDialog(
                context, { _, selectedHour, selectedMinute ->
                    viewModel.setDeadlineTime(selectedHour, selectedMinute)
                    showTimePicker = false
                },
                hour, minute, DateFormat.is24HourFormat(context)
            )
            dialog.setOnDismissListener { showTimePicker = false }
            dialog.show()
        }
    }
}

@Composable
fun IntInputField(
    value: Int, onValueChange: () -> Unit, label: () -> Unit, modifier: Modifier.Companion
) {
    TODO("Not yet implemented")
}


/*@Preview
@Composable
fun PreviewParentAddQuestScreen(){
    RPGTheme {
        ParentAddQuestScreen(modifier = modifier, overlayNavController = rememberNavController(), viewModel = hiltViewModel(), authViewModel = hiltViewModel())
    }
}*/