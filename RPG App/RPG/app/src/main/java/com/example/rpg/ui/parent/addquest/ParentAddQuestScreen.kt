package com.example.rpg.ui.parent.addquest

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.theme.RPGTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.parent.quest.ParentQuestScreen
import com.example.rpg.ui.parent.addquest.ParentAddQuestViewModel
import com.example.rpg.ui.theme.RPGTheme
import java.util.Calendar
import java.util.Date


/**
val title: String = "",
val description: String = "",
val assignDate: Date? = null,
val deadlineDate: Date? = null,
val completionDate : Date? = null,
val rewardAmount : Int = 0,
val rewardType : Reward = Reward.NONE,
val repeat : Boolean = false,
val allDay : Boolean = false,
val completed : Boolean = false,
val assignee : String = Child().id,
 */
@Composable
fun ParentAddQuestScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ParentAddQuestViewModel = hiltViewModel()
){
    addQuestContent()
}

@Composable
fun addQuestContent(
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)


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
            value = title,
            onValueChange = { title = it },
            label = { Text(text = "Title") },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Description") },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showDatePicker = true }) {
            Text(selectedDate?.toString() ?: "Select Completion Date")
        }

        /*
        var expanded by remember { mutableStateOf(false) }
        var selectedChild by remember { mutableStateOf<Child?>(null) }

        Button(onClick = { expanded = true }) {
            Text(selectedChild?.name ?: "Select Child")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            childrenList.forEach { child ->
                DropdownMenuItem(onClick = {
                    selectedChild = child
                    expanded = false
                }) {
                    Text(child.name)
                }
            }
        }

         */


        Button(onClick = {  }) {
            Text(selectedDate?.toString() ?: "Finish Assigning Quest")
        }

        if (showDatePicker) {
            DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate = Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDay)
                    }.time
                    showDatePicker = false
                },
                year,
                month,
                day
            ).show()
        }
    }
}

@Preview
@Composable
fun PreviewParentAddQuestScreen(){
    RPGTheme {
        ParentAddQuestScreen(navController = rememberNavController(), overlayNavController = rememberNavController())
    }
}