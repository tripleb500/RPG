package com.example.rpg.ui.child.quest

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.rpg.R
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class SortOrder { ASCENDING, DESCENDING }
enum class SortBy { DEADLINE, STATUS }

@RequiresApi(Build.VERSION_CODES.O)
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

    var sortBy by remember { mutableStateOf(SortBy.DEADLINE) }
    var sortOrder by rememberSaveable { mutableStateOf(SortOrder.ASCENDING) }

    var selectedQuest by remember { mutableStateOf<Quest?>(null) }

    var showCalendar by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

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
            FloatingActionButton(
                onClick = {
                    showCalendar = true
                },
                modifier = Modifier.width(100.dp)
            ) {
                Text(
                    text = "Calendar",
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

            val filtered = childQuests.value.filter { it.status == selectedTab }
            
            val sorted = when (sortBy) {
                SortBy.STATUS -> when (sortOrder) {
                    SortOrder.ASCENDING -> filtered.sortedBy { it.deadlineDate }
                    SortOrder.DESCENDING -> filtered.sortedByDescending { it.deadlineDate }
                }

                SortBy.DEADLINE -> when (sortOrder) {
                    SortOrder.ASCENDING -> filtered.sortedBy { it.deadlineDate }
                    SortOrder.DESCENDING -> filtered.sortedByDescending { it.deadlineDate }
                }
            }

            LazyColumn {
                items(sorted) { quest ->
                    CardView(
                        quest = quest,
                        selectedTab = selectedTab,
                        onQuestClick = { selectedQuest = quest }
                    )
                }
            }
        }
    }
    selectedQuest?.let { quest ->
        when (selectedTab) {
            Status.INPROGRESS -> ChildInProgressQuestDialog(
                quest = quest,
                onDismissRequest = { selectedQuest = null }, // ← Clear selected quest
                viewModel = viewModel, // ← Use the parent ViewModel
            )

            Status.PENDING -> ChildPendingQuestDialog(
                quest = quest,
                onDismissRequest = { selectedQuest = null },
                viewModel = viewModel
            )

            Status.COMPLETED -> ChildCompletedQuestDialog(
                quest = quest,
                onDismissRequest = { selectedQuest = null },
                viewModel = viewModel
            )

            Status.INCOMPLETE -> ChildIncompletedQuestDialog(
                quest = quest,
                onDismissRequest = { selectedQuest = null },
                viewModel = viewModel
            )

            else -> {}
        }
    }

    if (showCalendar) {
        // Only INPROGRESS quest deadlines
        val questDueDates: Set<LocalDate> = childQuests.value
            .filter { it.status == Status.INPROGRESS }
            .mapNotNull { quest ->
                quest.deadlineDate?.toInstant()
                    ?.atZone(ZoneId.systemDefault())
                    ?.toLocalDate()
            }.toSet()

        // Track the current month shown
        val currentMonth = remember { mutableStateOf(YearMonth.now()) }

        Dialog(onDismissRequest = { showCalendar = false }) {
            Card(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Month header with navigation arrows
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = {
                            currentMonth.value = currentMonth.value.minusMonths(1)
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Previous Month"
                            )
                        }

                        val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy") }
                        Text(
                            text = currentMonth.value.format(monthFormatter),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(onClick = {
                            currentMonth.value = currentMonth.value.plusMonths(1)
                        }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Custom calendar grid
                    CustomCalendar(
                        currentMonth = currentMonth.value,
                        highlightedDates = questDueDates,
                        selectedDate = selectedDate,
                        onDateSelected = { date ->
                            selectedDate = date
                            showCalendar = false
                        }
                    )
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendar(
    currentMonth: YearMonth,
    highlightedDates: Set<LocalDate>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDay = currentMonth.atDay(1)
    val lastDay = currentMonth.atEndOfMonth()
    val firstDayOfWeek = firstDay.dayOfWeek.value % 7

    val totalCells = lastDay.dayOfMonth + firstDayOfWeek
    val days = (1..totalCells).map { it }

    Column {
        // Weekday labels
        Row {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(240.dp), // adjust as needed
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(totalCells) { index ->
                if (index < firstDayOfWeek) {
                    Box(modifier = Modifier.size(36.dp)) {} // empty placeholder
                } else {
                    val dayOfMonth = index - firstDayOfWeek + 1
                    val date = currentMonth.atDay(dayOfMonth)
                    val isHighlighted = highlightedDates.contains(date)
                    val isSelected = selectedDate == date

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isSelected -> Color(0xFF1976D2)
                                    isHighlighted -> Color(0xFF42A5F5)
                                    else -> Color.Transparent
                                }
                            )
                            .clickable { onDateSelected(date) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayOfMonth.toString(),
                            color = if (isSelected || isHighlighted) Color.White else Color.Black,
                            textAlign = TextAlign.Center
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
    onQuestClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .clickable { onQuestClick() },
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