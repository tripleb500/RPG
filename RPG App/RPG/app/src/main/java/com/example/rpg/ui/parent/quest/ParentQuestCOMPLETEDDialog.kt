package com.example.rpg.ui.parent.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.rpg.data.model.Quest
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.parent.home.ParentHomeScreenViewModel
import com.example.rpg.R
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun CompletedQuestDialog(
    quest: Quest,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFB2DFDB),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.quest_completed),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        text = {
            val assignDateFormatted = quest.assignDate?.let {
                val formatter = SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault())
                formatter.format(it)
            } ?: "N/A"

            val completionDateFormatted = quest.completionDate?.let {
                val formatter = SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault())
                formatter.format(it)
            } ?: "N/A"
            Column {
                Text(stringResource(R.string.title_label, quest.title))
                Text(stringResource(R.string.description_label, quest.description))
                Text(stringResource(R.string.assigned_on_label, assignDateFormatted))
                Text(stringResource(R.string.completed_on_label, completionDateFormatted))
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}