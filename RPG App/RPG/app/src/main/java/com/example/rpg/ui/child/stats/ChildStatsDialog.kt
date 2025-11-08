package com.example.rpg.ui.child.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.child.home.ChildHomeScreenViewModel

const val totalXP = 340

const val currentXP = totalXP % 100

val StatsList = mutableStateListOf(
    statItems("Bradford", currentXP, 23, 8),
)

@Composable
fun ChildStatsDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    viewModel: ChildHomeScreenViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val isLoadingStats by remember { derivedStateOf { viewModel.isLoadingStats } }
    val errorMessagesStats by remember { derivedStateOf { viewModel.errorMessageStats } }

    val count by viewModel.completedQuestsCount.collectAsState()

    var username by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Stats",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge
                )
                LazyColumn {
                    items(StatsList) { Stats ->
                        Text(
                            text = Stats.name,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = "Current XP: " + Stats.currentXP,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = "Quests Completed: $count",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = "Quests Streak: " + Stats.questStreak,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (errorMessagesStats != null) {
                    Text(
                        text = errorMessagesStats ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
//                OutlinedTextField(
//                    value = accessCode,
//                    onValueChange = { accessCode = it },
//                    label = { Text("Access Code") },
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth()
//                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                }
            }
        }
    }
}