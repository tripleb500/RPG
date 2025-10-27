package com.example.rpg.ui.parent.quest

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Quest_Status
import com.example.rpg.ui.Routes
import com.example.rpg.ui.theme.RPGTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentQuestScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ParentQuestViewModel = hiltViewModel()
) {
    val childQuestMap = viewModel.questsByAssignee.collectAsState()

    var selectedTab by rememberSaveable { mutableStateOf(QuestTab.Overview) }

    Scaffold(
        // Topbar, visual signifier for users to know where they are.
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B2631)
                ),

                title = {
                    Text(
                        "Quests",
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                    )
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { overlayNavController.navigate(Routes.ParentAddQuestScreen.route) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Quest")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 85.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Create tabs
            QuestTabBar(selected = selectedTab, onSelect = { selectedTab = it })
            // Use when conditional to swap between what's being displayed
            when (selectedTab) {
                QuestTab.Overview -> {
                    LazyColumn {
                        childQuestMap.value.forEach { (assigneeId, quests) ->
                            // Maybe display the child's name here using assigneeId

                            items(quests) { quest ->
                                CardView(quest)
                            }
                        }
                    }
                }

                // quests with no status field in firebase currently defaults to inprogress tab (Quest.kt)
                QuestTab.Ongoing -> {
                    LazyColumn {
                        childQuestMap.value.forEach { (assigneeId, quests) ->
                            items(quests.filter { it.status == Quest_Status.INPROGRESS }) { quest ->
                                CardView(quest)
                            }
                        }
                    }
                }

                QuestTab.Pending -> {
                    LazyColumn {
                        childQuestMap.value.forEach { (assigneeId, quests) ->
                            items(quests.filter { it.status == Quest_Status.Pending }) { quest ->
                                CardView(quest)

                            }
                        }
                    }
                }

                QuestTab.Completed -> {
                    LazyColumn {
                        childQuestMap.value.forEach { (assigneeId, quests) ->
                            items(quests.filter { it.status == Quest_Status.COMPLETED }) { quest ->
                                CardView(quest)
                            }
                        }
                    }
                }

                QuestTab.Incompleted -> {
                    LazyColumn {
                        childQuestMap.value.forEach { (assigneeId, quests) ->
                            items(quests.filter { it.status == Quest_Status.INCOMPLETED }) { quest ->
                                CardView(quest)
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun CardView(quest: Quest) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = "Child avatar",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
            }

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = "Title: ${quest.title}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Description: ${quest.title}",
                )
                Text(
                    text = "Due Date: ${quest.deadlineDate}",
                )


            }
        }
    }
}

@Preview
@Composable
fun PreviewParentQuestScreen() {
    RPGTheme {
        ParentQuestScreen(
            navController = rememberNavController(),
            overlayNavController = rememberNavController()
        )
    }
}

// Individual Tabs
enum class QuestTab { Overview, Ongoing, Pending, Completed, Incompleted }

// Tab Bar
@Composable
private fun QuestTabBar(
    selected: QuestTab,
    onSelect: (QuestTab) -> Unit
) {
    Row(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        QuestTab.entries.forEach { tab ->
            Button(onClick = { onSelect(tab) }) {
                Text(text = tab.name)
            }
        }
    }
}
