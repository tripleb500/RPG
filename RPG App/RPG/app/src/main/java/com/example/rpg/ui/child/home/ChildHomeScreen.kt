package com.example.rpg.ui.child.home
// TODO: button for stats; achievements, list of main quests
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import com.example.rpg.ui.child.achievements.ChildAchievementsDialog
import com.example.rpg.ui.child.quest.ChildInProgressQuestDialog
import com.example.rpg.ui.child.stats.ChildStatsDialog
import java.text.SimpleDateFormat
import java.util.Locale

enum class SortOrder { ASCENDING, DESCENDING }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ChildHomeScreenViewModel = hiltViewModel(),
) {
    val user by viewModel.currentUserFlow.collectAsState(initial = null)
    val stats by viewModel.statsFlow.collectAsState()

    var showDialogAchievements by remember { mutableStateOf(false) }
    var showDialogStats by remember { mutableStateOf(false) }
    val questList = viewModel.childQuests.collectAsState()
    var selectedQuest by remember { mutableStateOf<Quest?>(null) }
    var sortOrder by rememberSaveable { mutableStateOf(SortOrder.ASCENDING) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            //color tuple needs to be updated once material theming implemented
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF798B6A), Color(0xFF6c7d5f))
                )
            )
    ) {
        // header of child, has profile picture, name, level/xp, streak
        // 2 clickable cards: achievements and stats
        // list of ongoing quests
        Column {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B2631)
                ),

                title = {
                    Text(
                        text = user?.username ?: "Loading...",
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Image(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        contentDescription = "Photo of Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Level info
                    Column {
                        val level = stats.totalXP / 100
                        val xpForNextLevel = stats.totalXP % 100

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Level: $level",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "$xpForNextLevel / 100",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Progress bar
                        LinearProgressIndicator(
                            progress = { xpForNextLevel / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = Color(0xFF2B6A2B),      // green
                            trackColor = Color(0xFFBBDEFB),  // light blue
                            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = 45.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Entire card is now the tap target
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2B6A2B)
                    ),
                    onClick = { showDialogAchievements = true }, // open dialog on card tap
                ) {
                    Row(Modifier.padding(16.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_trophy_24),
                            contentDescription = "Photo of achievement symbol",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp),
                            colorFilter = ColorFilter.tint(Color(0xFFFFD700)) // gold color
                        )
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp),
                            verticalArrangement = Arrangement.Center

                        ) {
                            Text(
                                text = "Achievements",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2B6A2B)
                    ),
                    onClick = { showDialogStats = true }, // open dialog on card tap
                ) {
                    Row(Modifier.padding(16.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_auto_graph_24),
                            contentDescription = "Photo of stat symbol",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp),
                            colorFilter = ColorFilter.tint(Color.White) // gold color
                        )
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Stats",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp
                            )
                        }
                    }
                }

                if (showDialogAchievements) {
                    ChildAchievementsDialog(
                        onDismissRequest = { showDialogAchievements = false },
                        viewModel = viewModel,
                    )
                }

                if (showDialogStats) {
                    ChildStatsDialog(
                        onDismissRequest = { showDialogStats = false },
                        viewModel = viewModel,
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A4A17)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {

                        Text(
                            text = stringResource(R.string.main_quest),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }

                val filtered = questList.value.filter { it.status == Status.INPROGRESS }

                val sortedQuests = when (sortOrder) {
                    SortOrder.ASCENDING -> filtered.sortedBy { it.deadlineDate }
                    SortOrder.DESCENDING -> filtered.sortedByDescending { it.deadlineDate }
                }

                LazyColumn {
                    items(sortedQuests) { quest ->
                        CardView(
                            quest = quest,
                            onQuestClick = { selectedQuest = quest }
                        )
                    }
                }
            }
        }

        // Quest completion dialog
        if (selectedQuest != null) {
            ChildInProgressQuestDialog(
                quest = selectedQuest!!,
                onDismissRequest = { selectedQuest = null },
            )
        }
    }
}

@Composable
fun CardView(
    quest: Quest,
    onQuestClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onQuestClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFBBDEFB)
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = quest.imageURL.ifBlank { null },
                contentDescription = "Quest Image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 12.dp),
                placeholder = painterResource(R.drawable.rpg_logo_parent),
                error = painterResource(R.drawable.rpg_logo_parent)
            )

            Column {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append(stringResource(R.string.title_colon_label))
                        }
                        append(quest.title)
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append(stringResource(R.string.description_colon_label))
                        }
                        append(quest.description)
                    }
                )

                if (quest.status == Status.COMPLETED) {
                    val formattedCompletionDate = quest.completionDate?.let {
                        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        formatter.format(it)
                    } ?: "N/A"

                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(R.string.completed_on_colon_label))
                            }
                            append(formattedCompletionDate)
                        }
                    )
                } else {
                    val formattedDate = quest.deadlineDate?.let {
                        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        formatter.format(it)
                    } ?: "N/A"
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(R.string.due_date_colon_label))
                            }
                            append(formattedDate)
                        }
                    )
                }
            }
        }
    }
}