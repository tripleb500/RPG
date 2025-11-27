package com.example.rpg.ui.child.home
// TODO: button for stats; achievements, list of main quests
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.child.achievements.ChildAchievementsDialog
import com.example.rpg.ui.child.quest.CardView
import com.example.rpg.ui.child.quest.ChildInProgressQuestDialog
import com.example.rpg.ui.child.quest.ChildQuestViewModel
import com.example.rpg.ui.child.quest.SortOrder
import com.example.rpg.ui.child.stats.ChildStatsDialog
import com.example.rpg.ui.parent.home.Family
import com.example.rpg.ui.parent.home.ProgressIndicator
import com.example.rpg.ui.parent.quest.InProgressQuestDialog
import com.example.rpg.ui.parent.quest.ParentQuestViewModel
import com.example.rpg.ui.theme.RPGTheme
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
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val user by viewModel.currentUserFlow.collectAsState(initial = null)
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
            Row {
                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = "Photo of Avatar",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
                Column(
                    modifier = Modifier.padding(top = 50.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    ProgressIndicator(
                        progress = 99999999f
                    )
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
fun ProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier,
    )
}

@Composable
fun CardView(
    quest: Quest,
    onQuestClick: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }


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
                model = if (quest.imageURL.isNotBlank()) quest.imageURL else null,
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

@Preview
@Composable
fun PreviewChildHomeScreen() {
    RPGTheme {
        ChildHomeScreen(
            navController = rememberNavController(),
            overlayNavController = rememberNavController()
        )
    }
}
