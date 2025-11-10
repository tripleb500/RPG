package com.example.rpg.ui.parent.quest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import com.example.rpg.ui.Routes
import com.example.rpg.ui.theme.RPGTheme
import java.text.SimpleDateFormat
import java.util.Locale

enum class SortOrder { ASCENDING, DESCENDING }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentQuestScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ParentQuestViewModel = hiltViewModel()
) {
    val assignedQuests = viewModel.assignedQuests.collectAsState()
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
                        text = stringResource(R.string.quests),
                        fontSize = 32.sp,
                        maxLines = 1,
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
            FloatingActionButton(
                onClick = { overlayNavController.navigate(Routes.ParentAddQuestScreen.route) },
                modifier = Modifier.width(100.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_quest),
                    modifier = Modifier,
                    fontSize = 16.sp
                )
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
            // Filter and sort quests
            val filtered = assignedQuests.value.filter { it.status == selectedTab }

            val sorted = when (sortOrder) {
                SortOrder.ASCENDING -> filtered.sortedBy { it.deadlineDate }
                SortOrder.DESCENDING -> filtered.sortedByDescending { it.deadlineDate }
            }

            val groupedByChild = sorted.groupBy { it.assignedTo }

            LazyColumn {
                groupedByChild.forEach { (childId, quests) ->
                    val childName by viewModel.getQuestChildName(childId)
                    item {
                        // Child name header
                        Text(
                            text = childName ?: stringResource(R.string.unknown),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    // Display each quest for that child
                    items(quests) { quest ->
                        CardView(
                            quest = quest,
                            selectedTab = selectedTab,
                            viewModel = viewModel
                        )
                    }
                }
//
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
//                        childQuestMap.value.forEach { (assigneeId, quests) ->
//                            items(quests.filter { it.status == Status.INPROGRESS }) { quest ->
//                                CardView(quest)
//                            }
//                        }
//                    }
//                }
//
//                Status.PENDING -> {
//                    LazyColumn {
//                        childQuestMap.value.forEach { (assigneeId, quests) ->
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
//                        childQuestMap.value.forEach { (assigneeId, quests) ->
//                            items(quests.filter { it.status == Status.COMPLETED }) { quest ->
//                                CardView(quest)
//                            }
//                        }
//                    }
//                }
//
//                Status.INCOMPLETED -> {
//                    LazyColumn {
//                        childQuestMap.value.forEach { (assigneeId, quests) ->
//                            items(quests.filter { it.status == Status.INCOMPLETED }) { quest ->
//                                CardView(quest)
//                            }
//                        }
//                    }
//                }

            }
        }
    }
}


@Composable
fun CardView(
    quest: Quest,
    selectedTab: Status,
    viewModel: ParentQuestViewModel = hiltViewModel()
) {
    // Collect child name as state
    val assignedToName by viewModel.getQuestChildName(quest.assignedTo)
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }

    // InProgress Edit Dialog
    if (showEditDialog) {
        EditQuestDialog(
            quest = quest,
            onSave = { updatedQuest ->
                viewModel.updateQuestDetails(updatedQuest)
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }

    // Base Dialogs TODO: Finish this
    if (showDialog) {
        when (selectedTab) {
            Status.AVAILABLE -> AvailableQuestDialog(
                quest = quest,
                onApprove = {viewModel.updateQuestStatus(quest.id, Status.COMPLETED)},
                onReject = {viewModel.updateQuestStatus(quest.id, Status.AVAILABLE)},
                onDismiss = {showDialog = false}
            )
            Status.INPROGRESS -> InProgressQuestDialog(
                quest = quest,
                onApprove = {
                    // Mark quest as completed
                    viewModel.updateQuestStatus(quest.id, Status.COMPLETED)
                },
                onEdit = {
                    showDialog = false
                    showEditDialog = true
                },
                onDismiss = { showDialog = false }
            )
            Status.PENDING -> PendingQuestDialog(
                quest = quest,
                onApprove = { viewModel.updateQuestStatus(quest.id, Status.COMPLETED) },
                onReject = { viewModel.updateQuestStatus(quest.id, Status.INCOMPLETE) },
                onDismiss = { showDialog = false }
            )
            Status.COMPLETED -> CompletedQuestDialog(
                quest = quest,
                onApprove = { viewModel.updateQuestStatus(quest.id, Status.COMPLETED) },
                onReject = { viewModel.updateQuestStatus(quest.id, Status.INPROGRESS) },
                onDismiss = { showDialog = false }
            )
            Status.INCOMPLETE -> IncompleteQuestDialog(
                quest = quest,
                onApprove = { viewModel.updateQuestStatus(quest.id, Status.COMPLETED) },
                onReject = { viewModel.updateQuestStatus(quest.id, Status.INPROGRESS) },
                onDismiss = { showDialog = false }
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(
            containerColor = when (quest.status) {
                Status.COMPLETED -> Color(0xFFB2DFDB)
                Status.PENDING -> Color(0xFFFFF9C4)
                Status.INPROGRESS -> Color(0xFFBBDEFB)
                Status.INCOMPLETE -> Color(0xFFFFCDD2)
                else -> Color.White
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "Child avatar",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 12.dp)
            )
            val formattedDate = quest.deadlineDate?.let {
                val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                formatter.format(it)
            } ?: "N/A"
            Column {
                Text(stringResource(R.string.title, quest.title))
                Text(stringResource(R.string.description, quest.description))
                Text(stringResource(R.string.due_date, formattedDate))
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
    fun statusColor(status: Status) = when (status) {
        Status.COMPLETED -> Color(0xFFB2DFDB)
        Status.PENDING -> Color(0xFFFFF9C4)
        Status.INPROGRESS -> Color(0xFFBBDEFB)
        Status.INCOMPLETE -> Color(0xFFFFCDD2)
        else -> Color.White
    }

    ScrollableTabRow(
        selectedTabIndex = selected.ordinal,
        edgePadding = 0.dp,
        indicator = {}
    ) {
        Status.entries.forEachIndexed { index, tab ->
            Tab(
                selected = selected.ordinal == index,
                onClick = {
                    onSelect(tab)
                },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .background(
                        color = if (tab == selected) statusColor(tab) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ),
                text = {
                    Text(
                        text = tab.name.replace("_", " "),
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            )
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