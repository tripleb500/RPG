package com.example.rpg.ui.child.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.rpg.ui.child.home.ChildHomeScreenViewModel

val questachievements = mutableStateListOf(
    questAchievements("Go Getter", "Get 1 Quests Completed", 1),
    questAchievements("Go Getter2", "Get 10 Quests Completed", 10),
    questAchievements("Go Getter3", "Get 20 Quests Completed", 20),
    questAchievements("Go Getter4", "Get 30 Quests Completed", 30),
    questAchievements("Go Getter5", "Get 40 Quests Completed", 40),
)
val levelachievements = mutableStateListOf(
    levelAchievements("Level Up!!!", "Reach level 1", 1),
    levelAchievements("Level Up5!!!", "Reach level 5", 5),
    levelAchievements("Level Up10!!!", "Reach level 10", 10),
    levelAchievements("Level Up25!!!", "Reach level 25", 25),
    levelAchievements("Level Up50!!!", "Reach level 50", 50),
)

@Composable
fun ChildAchievementsDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    viewModel: ChildHomeScreenViewModel = hiltViewModel(),
) {
    val errorMessagesAchievements by remember { derivedStateOf { viewModel.errorMessageAchievements } }

    val count by viewModel.completedQuestsCount.collectAsState()
    val level by viewModel.currentLevel.collectAsState()
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
                    text = "Achievements",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge
                )

                LazyColumn {
                    items(questachievements) { Achievement ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = if (count >= Achievement.achAmount) Color(0xFF6c7d5f) else Color(
                                        0xFFBBDEFB
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = Achievement.achName,
                                    fontWeight = FontWeight.SemiBold
                                )
//                                Text(
//                                    text = "Earned on: " + Achievement.achDate,
//                                    style = MaterialTheme.typography.bodySmall,
//                                )
                                Text(
                                    text = Achievement.achDesc,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
                LazyColumn {
                    items(levelachievements) { Achievement ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = if (level >= Achievement.achAmount) Color(0xFF6c7d5f) else Color(
                                        0xFFBBDEFB
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = Achievement.achName,
                                    fontWeight = FontWeight.SemiBold
                                )
//                                Text(
//                                    text = "Earned on: " + Achievement.achDate,
//                                    style = MaterialTheme.typography.bodySmall,
//                                )
                                Text(
                                    text = Achievement.achDesc,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
                if (errorMessagesAchievements != null) {
                    Text(
                        text = errorMessagesAchievements ?: "",
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
                        Text("Close")
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                }
            }
        }
    }
}