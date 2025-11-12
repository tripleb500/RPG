package com.example.rpg.ui.child.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChildIncompletedQuestDialog(
    quest: Quest,
    viewModel: ChildQuestViewModel,
    onDismissRequest: () -> Unit
) {
    val assigneeName by viewModel.getQuestParentName(quest.assignee)

    val imageUrl by viewModel.imageUrl.collectAsState()

    Dialog(onDismissRequest = onDismissRequest) {
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
                            color = Color(0xFFFFCDD2),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.quest_failed),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Quest information
                CompositionLocalProvider(LocalContentColor provides Color.Black) {
                    val assignDateFormatted = quest.assignDate?.let {
                        SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault()).format(it)
                    } ?: "N/A"

                    val completionDateFormatted = quest.completionDate?.let {
                        SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault()).format(it)
                    } ?: "N/A"

                    val deadlineDateFormatted = quest.deadlineDate?.let {
                        SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault()).format(it)
                    } ?: "N/A"

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append("Assigned By: ") }
                                append(assigneeName)
                            },
                            color = Color.Black
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append("Title: ") }
                                append(quest.title)
                            },
                            color = Color.Black
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append("Description: ") }
                                append(quest.description)
                            },
                            color = Color.Black
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append("Assigned On: ") }
                                append(assignDateFormatted)
                            },
                            color = Color.Black
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append("Due Date: ") }
                                append(deadlineDateFormatted)
                            },
                            color = Color.Black
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append("Completed On: ") }
                                append(completionDateFormatted)
                            },
                            color = Color.Black
                        )
                    }
                }

                AsyncImage(
                    model = if (quest.imageURL.isNotBlank()) quest.imageURL else null,
                    contentDescription = "Quest Image",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(end = 12.dp),
                    placeholder = painterResource(R.drawable.rpg_logo_parent),
                    error = painterResource(R.drawable.rpg_logo_parent)
                )

                // OK button
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("OK", textAlign = TextAlign.Center)
                }
            }
        }
    }
}