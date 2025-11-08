package com.example.rpg.ui.child.achievements

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Reward
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.child.home.ChildHomeScreenViewModel
import com.example.rpg.ui.child.quest.CardView
import com.example.rpg.ui.child.achievements.achievementItems
import java.util.Date

val sdf = SimpleDateFormat("dd/M/yyyy")
val dateAch = sdf.format(Date())
val AchievementsList = mutableStateListOf(
    achievementItems("Go Getter", dateAch, "Get 10 Quests Completed"),
    achievementItems("Go Getter2", dateAch, "Get 20 Quests Completed"),
    achievementItems("Go Getter3", dateAch, "Get 30 Quests Completed"),
    achievementItems("Go Getter4", dateAch, "Get 40 Quests Completed"),
    achievementItems("Go Getter5", dateAch, "Get 50 Quests Completed"),
    achievementItems("Level Up!!!", dateAch, "Reach level 2"),
    achievementItems("Level Up2!!!", dateAch, "Reach level 3"),
    achievementItems("Level Up3!!!", dateAch, "Reach level 4"),
    achievementItems("Level Up4!!!", dateAch, "Reach level 5"),
    achievementItems("Level Up5!!!", dateAch, "Reach level 6"),

    )

@Composable
fun ChildAchievementsDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    viewModel: ChildHomeScreenViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val isLoadingAchievements by remember { derivedStateOf { viewModel.isLoadingAchievements } }
    val errorMessagesAchievements by remember { derivedStateOf { viewModel.errorMessageAchievements } }

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
                    text = "Achievements",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge
                )
                LazyColumn {
                    items(AchievementsList) { Achievement ->
                        Text(
                            text = Achievement.achName,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = "Earned on: " + Achievement.achDate,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = Achievement.achDesc,
                            style = MaterialTheme.typography.bodySmall,
                        )
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