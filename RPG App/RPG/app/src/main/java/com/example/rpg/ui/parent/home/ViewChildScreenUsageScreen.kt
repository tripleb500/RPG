package com.example.rpg.ui.parent.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewChildScreenUsageScreen(
    childId: String,
    viewModel: ViewChildScreenUsageViewModel = hiltViewModel()
) {
    val usage by viewModel.childUsage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var minutes by remember { mutableStateOf("") }

    val isEnabled by viewModel.isEnabled.collectAsState()


    LaunchedEffect(childId) {
        viewModel.observeChildScreenUsage(childId)
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B2631)
                ),
                title = {
                    Text(
                        text = "Screen Time History: ",
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (error != null) {
                Text("Error: $error", color = Color.Red)
            } else {
                //Text("Screen Time History", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(usage) { record ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val formattedDate = formatDate(record.date)
                                Text(
                                    text = formattedDate,
                                    modifier = Modifier.weight(1f),
                                )
                                Text(
                                    text = "${record.screenTimeMs / 1000 / 60} min"
                                )
                            }
                        }

                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = minutes,
                            onValueChange = { newValue ->
                                // Allow only numbers
                                if (newValue.all { it.isDigit() }) {
                                    minutes = newValue
                                }
                            },
                            label = { Text("Set Screen Time") },
                            placeholder = { Text("Enter minutes") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                        )
                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled && minutes != "") {
                                    viewModel.updateScreenTimeLimit(childId, minutes)
                                } else {
                                    viewModel.updateScreenTimeLimit(childId, "0")
                                }
                            }
                        )
                    }
                }
            }


        }
    }

}

fun formatDate(dateString: String): String {
    return try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = originalFormat.parse(dateString)
        val newFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        date?.let {newFormat.format(it)  } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}