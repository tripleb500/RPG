package com.example.rpg.ui.child.quest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.rpg.data.model.Quest
import com.example.rpg.data.repository.UserRepository

@Composable
fun QuestDialog(
    quest: Quest,
//    userRepository: UserRepository = hiltViewModel(),
    onDismissRequest: () -> Unit,
    onCompleteClicked: (Quest) -> Unit
) {
    var parentName by remember { mutableStateOf("Loading...") }

    // Load parent info when the dialog is first composed
//    LaunchedEffect(quest.assignee) {
//        quest.assignee?.let { parentId ->
//            val parentUser = userRepository.getUserByUid(parentId) // suspend function
//            parentName = parentUser?.firstname ?: "Unknown"
//        }
//    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title
                Text(
                    text = quest.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Reward
                Text("Reward: ${quest.rewardType}")

                // Amount
                Text("Amount: ${quest.rewardAmount}")

                // Due Date
                Text("Due Date: ${quest.deadlineDate ?: "No deadline"}")

                // Assignee
                Text("Assigned by: blah")

                Spacer(Modifier.height(16.dp))

                // Complete button
                Button(
                    onClick = {
                        onCompleteClicked(quest)
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mark as Completed")
                }

                // Cancel
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancel", color = Color.Red)
                }
            }
        }
    }
}
