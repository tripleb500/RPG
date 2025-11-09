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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.child.achievements.ChildAchievementsDialog
import com.example.rpg.ui.child.quest.QuestDialog
import com.example.rpg.ui.child.stats.ChildStatsDialog
import com.example.rpg.ui.parent.home.Family
import com.example.rpg.ui.parent.home.ProgressIndicator
import com.example.rpg.ui.theme.RPGTheme

// mock data
val child = Family("Bradford", 1, 0.1F)


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
    val questList by viewModel.inProgressQuestsFlow.collectAsState(initial = emptyList())
    var selectedQuest by remember { mutableStateOf<Quest?>(null) }

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
                        progress = child.lvlProgress
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
                    onClick = { showDialogAchievements = true }, // open dialog on card tap
                ) {
                    Row(Modifier.padding(16.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_photo_camera_back_24),
                            contentDescription = "Photo of achievement symbol",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                        )
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp),
                            verticalArrangement = Arrangement.Center

                        ) {
                            Text(
                                text = "Achievements",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .padding(12.dp),
                    onClick = { showDialogStats = true }, // open dialog on card tap
                ) {
                    Row(Modifier.padding(16.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_photo_camera_back_24),
                            contentDescription = "Photo of stat symbol",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                        )
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Stats",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                if (showDialogAchievements) {
                    ChildAchievementsDialog(
                        onDismissRequest = { showDialogAchievements = false },
                        viewModel = viewModel,
                        authViewModel = authViewModel
                    )
                }

                if (showDialogStats) {
                    ChildStatsDialog(
                        onDismissRequest = { showDialogStats = false },
                        viewModel = viewModel,
                        authViewModel = authViewModel
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
                LazyColumn {
                    items(
                        items = questList,
                        key = { quest -> quest.id }
                    ) { quest ->
                        CardView(quest) { clickedQuest ->
                            selectedQuest = clickedQuest
                        }
                    }
                }

            }
        }

        // Quest completion dialog
        if (selectedQuest != null) {
            QuestDialog(
                quest = selectedQuest!!,
                onDismissRequest = { selectedQuest = null },
                viewModel = viewModel,
                onCompleteClicked = { completedQuest ->
                    viewModel.markQuestAsPending(completedQuest)
                    selectedQuest = null
                }
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
    onQuestClicked: (Quest) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .clickable { onQuestClicked(quest) }
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.outline_photo_camera_back_24),
                contentDescription = "Photo of quest",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Column {
                Text(
                    text = quest.title,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Reward: ${quest.rewardType}",
                )
                Text(
                    text = "Due: ${quest.deadlineDate}",
                )
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
