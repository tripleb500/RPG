package com.example.rpg.ui.parent.quest

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                    val childNameState by viewModel.getQuestChildName(childId)
                    val childName = childNameState

                    if (!childName.isNullOrBlank()) {
                        item {
                            // Child name header
                            Text(
                                text = childName,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 32.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
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
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var questImage by rememberSaveable { mutableStateOf("") }

    // InProgress Edit Dialog
    if (showEditDialog) {
        EditQuestDialog(
            quest = quest,
            onSave = { updatedQuest ->
                viewModel.updateQuestDetails(updatedQuest)
                showEditDialog = false
            },
            onDelete = { questToDelete ->
                viewModel.deleteQuest(questToDelete.id)
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }

    if (showDialog) {
        when (selectedTab) {
            Status.AVAILABLE -> AvailableQuestDialog(
                quest = quest,
                onEdit = {
                    showDialog = false
                    showEditDialog = true
                },
                onCancel = { showDialog = false }
            )

            Status.INPROGRESS -> InProgressQuestDialog(
                quest = quest,
                onApprove = {
                    // Mark quest as completed
                    viewModel.updateQuestStatus(quest.id, Status.COMPLETED)
                    viewModel.completeQuest(quest.id)
                },
                onEdit = {
                    showDialog = false
                    showEditDialog = true
                },
                onDismiss = { showDialog = false }
            )

            Status.PENDING -> PendingQuestDialog(
                quest = quest,
                onApprove = {
                    viewModel.updateQuestStatus(quest.id, Status.COMPLETED)
                    viewModel.completeQuest(quest.id)
                },
                onReject = { viewModel.updateQuestStatus(quest.id, Status.INCOMPLETE) },
                onReassign = {
                    viewModel.updateQuestDetails(it.copy(status = Status.INPROGRESS))
                    viewModel.setAssignDate(quest.id)
                },
                onDismiss = { showDialog = false }
            )

            Status.COMPLETED -> CompletedQuestDialog(
                quest = quest,
                onDismiss = { showDialog = false }
            )

            Status.INCOMPLETE -> IncompleteQuestDialog(
                quest = quest,
                onCancel = { showDialog = false },
                onReassign = { questToReassign ->
                    viewModel.updateQuestStatus(questToReassign.id, Status.INPROGRESS)
                    viewModel.setAssignDate(quest.id)
                    showDialog = false
                },
                onDelete = { questToDelete ->
                    viewModel.deleteQuest(questToDelete.id)
                    showDialog = false
                }
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