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
import androidx.compose.foundation.layout.size
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
import coil.compose.AsyncImage
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import com.example.rpg.ui.Routes
import com.example.rpg.ui.child.home.ChildHomeScreenViewModel
import com.example.rpg.ui.parent.quest.ParentQuestViewModel
import com.example.rpg.ui.parent.quest.PendingQuestDialog

enum class SortOrder { ASCENDING, DESCENDING }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildQuestScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ChildHomeScreenViewModel = hiltViewModel()
) {
    val childQuests = viewModel.childQuests.collectAsState()
    var selectedTab by rememberSaveable { mutableStateOf(Status.INPROGRESS) }
    var sortOrder by rememberSaveable { mutableStateOf(SortOrder.ASCENDING) }

    var selectedQuest by remember { mutableStateOf<Quest?>(null) }

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
                    CardView(
                        quest = quest,
                        selectedTab = selectedTab,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun CardView(
    quest: Quest,
    viewModel: ChildHomeScreenViewModel = hiltViewModel(),
    selectedTab: Status) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        when (selectedTab) {
            Status.INPROGRESS -> ChildInProgressQuestDialog(
                quest = quest,
                onDismissRequest = {showDialog = false},
                onCompleteClicked = {
                    completedQuest ->
                    viewModel.markQuestAsPending(completedQuest)
                },
                viewModel = viewModel
            )
            Status.PENDING -> ChildPendingQuestDialog(
                quest = quest,
                onDismissRequest = {showDialog = false},
                viewModel = viewModel
            )
            Status.COMPLETED -> ChildPendingQuestDialog(
                quest = quest,
                onDismissRequest = {showDialog = false},
                viewModel = viewModel
            )
            Status.INCOMPLETE -> ChildPendingQuestDialog(
                quest = quest,
                onDismissRequest = {showDialog = false},
                viewModel = viewModel
            )
            else -> {}
        }
    }


    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(
            containerColor = when (selectedTab) {
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

            AsyncImage(
                model = if (quest.imageURL.isNotBlank()) quest.imageURL else null,
                contentDescription = "Quest Image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 12.dp),
                placeholder = painterResource(R.drawable.rpg_logo_parent),
                error = painterResource(R.drawable.rpg_logo_parent)
            )

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