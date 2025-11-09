package com.example.rpg.ui.child.quest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import com.example.rpg.ui.Routes
import com.example.rpg.ui.parent.quest.ParentQuestViewModel
import com.example.rpg.ui.parent.quest.PendingQuestDialog

enum class SortOrder { ASCENDING, DESCENDING }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildQuestScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ChildQuestViewModel = hiltViewModel()
) {
    val childQuests = viewModel.childQuests.collectAsState()
    var selectedTab by rememberSaveable { mutableStateOf(Status.INPROGRESS) }
    var sortOrder by rememberSaveable { mutableStateOf(SortOrder.ASCENDING) }

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
                },
                actions = {
                    IconButton(onClick = {
                        sortOrder = if (sortOrder == SortOrder.ASCENDING)
                            SortOrder.DESCENDING else SortOrder.ASCENDING
                    }) {
                        val icon = if (sortOrder == SortOrder.ASCENDING)
                            Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
                        Icon(icon, contentDescription = "Sort by deadline")
                    }
                }

            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { overlayNavController.navigate(Routes.ParentAddQuestScreen.route) },
                modifier = Modifier.width(100.dp)) {
                Text(text = "Add Quest",
                    modifier = Modifier,
                    fontSize = 16.sp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Create tabs
            QuestTabBar(selected = selectedTab, onSelect = { selectedTab = it })

            val filtered = childQuests.value.filter { it.status == selectedTab }

            val sorted = when (sortOrder) {
                SortOrder.ASCENDING -> filtered.sortedBy { it.deadlineDate }
                SortOrder.DESCENDING -> filtered.sortedByDescending { it.deadlineDate }
            }

            LazyColumn {
                items(sorted) { quest ->
                    CardView(quest)
                }
            }

//            // Use when conditional to swap between what's being displayed
//            when (selectedTab) {
////                QuestTab.Overview -> {
////                    LazyColumn {
////                        childQuestMap.value.forEach { (assigneeId, quests) ->
////                            // Maybe display the child's name here using assigneeId
////
////                            items(quests) { quest ->
////                                CardView(quest)
////                            }
////                        }
////                    }
////                }
//
//                // quests with no status field in firebase currently defaults to inprogress tab (Quest.kt)
//                Status.INPROGRESS -> {
//                    LazyColumn {
//                        childQuestMap.value.forEach { (quests) ->
//                            items(quests.filter { it.status == Status.INPROGRESS }) { quest ->
//                                CardView(quest)
//                            }
//                        }
//                    }
//                }
//
//                Status.PENDING -> {
//                    LazyColumn {
//                        childQuestMap.value.forEach { (quests) ->
//                            items(quests.filter { it.status == Status.PENDING }) { quest ->
//                                CardView(quest)
//
//                            }
//                        }
//                    }
//                }
//
//                Status.COMPLETED -> {
//                    LazyColumn {
//                        childQuestMap.value.forEach { (quests) ->
//                            items(quests.filter { it.status == Status.COMPLETED }) { quest ->
//                                CardView(quest)
//                            }
//                        }
//                    }
//                }
//
//                Status.INCOMPLETED -> {
//                    LazyColumn {
//                        childQuestMap.value.forEach { (quests) ->
//                            items(quests.filter { it.status == Status.INCOMPLETED }) { quest ->
//                                CardView(quest)
//                            }
//                        }
//                    }
//                }

        }
    }
}

@Composable
fun CardView(quest: Quest, viewModel: ParentQuestViewModel = hiltViewModel()) {
    val assignedToName by viewModel.getQuestChildName(quest.assignedTo)
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        PendingQuestDialog(
            quest = quest,
            onApprove = {
                viewModel.updateQuestStatus(quest.id, Status.COMPLETED)
            },
            onReject = {
                viewModel.updateQuestStatus(quest.id, Status.INPROGRESS)
            },
            onDismiss = { showDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .then(
                if (quest.status == Status.PENDING)
                    Modifier
                        .clickable { showDialog = true } // parent clicks pending quest
                else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = when (quest.status) {
                Status.COMPLETED -> Color(0xFFB2DFDB)
                Status.PENDING -> Color(0xFFFFF9C4)
                Status.INPROGRESS -> Color(0xFFBBDEFB)
                Status.INCOMPLETED -> Color(0xFFFFCDD2)
                else -> Color.White
            }
        )
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
                    text = "Description: ${quest.description}",
                )
                Text(
                    text = "Due Date: ${quest.deadlineDate}",
                )
            }
        }
    }
}

// Tab Bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestTabBar(
    selected: Status,
    onSelect: (Status) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(selected.ordinal) }

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 8.dp
    ) {
        Status.entries.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    onSelect(tab)
                },
                text = {
                    Text(
                        text = tab.name.replace("_", " "),
                        maxLines = 1,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    }
}