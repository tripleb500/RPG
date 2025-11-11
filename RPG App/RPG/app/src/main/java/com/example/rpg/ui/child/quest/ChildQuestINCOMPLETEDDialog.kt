package com.example.rpg.ui.child.quest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.ui.child.home.ChildHomeScreenViewModel

@Composable
fun ChildIncompletedQuestDialog(
    quest: Quest,
    viewModel: ChildHomeScreenViewModel,
    onDismissRequest: () -> Unit
) {
    val assigneeName by viewModel.getQuestParentName(quest.assignee)

    val imageUrl by viewModel.imageUrl.collectAsState()


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
                Text(stringResource(R.string.reward_label, quest.rewardType))

                // Amount
                Text(stringResource(R.string.amount_label, quest.rewardAmount))

                // Due Date
                Text(stringResource(R.string.due_date_label, quest.deadlineDate ?: "No deadline"))

                // Assignee
                Text(
                    stringResource(
                        R.string.assigned_by_label,
                        assigneeName ?: stringResource(R.string.loading)
                    )
                )

                Spacer(Modifier.height(16.dp))

                if (imageUrl != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Photo",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Uploaded image",
                        modifier = Modifier.size(200.dp)
                    )
                }

                //Will display the image is there is on

//                questImage?.let { bitmap ->
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "Photo Captured:",
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Image(
//                        bitmap = bitmap.asImageBitmap(),
//                        contentDescription = "Captured quest photo",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(150.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
//                }



                // Close
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close", color = Color.Red)
                }
            }
        }
    }
}