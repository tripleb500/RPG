package com.example.rpg.ui.parent.stats

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.R
import com.example.rpg.data.model.Status
import com.example.rpg.data.model.User
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.parent.home.ParentHomeScreenViewModel
import com.example.rpg.ui.parent.quest.ParentQuestViewModel
import com.example.rpg.ui.theme.RPGTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentStatsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ParentHomeScreenViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    statViewModel: ParentStatsViewModel = viewModel(),
) {
    val children by viewModel.children.collectAsState()
    val parentId = authViewModel.currentUser?.uid


    LaunchedEffect(parentId) {
        if (parentId != null) {
            viewModel.loadChildren(parentId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B2631)
                ),

                title = {
                    Text(
                        "Stats",
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                    )
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            childStats(children)
        }
    }
}

@Composable
fun childStats(
    children: List<User>,
    questViewModel: ParentQuestViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
    ){

    var currentChildIndex by remember { mutableStateOf(0) }

    if (children.isEmpty()) {
        Text("No children available")
        return
    }

    val currentChild = children[currentChildIndex]
    val assignedQuests = questViewModel.assignedQuests.collectAsState()
    val completed = assignedQuests.value.filter { it.status == Status.COMPLETED }
    val inProgress = assignedQuests.value.filter { it.status == Status.INPROGRESS }
    val completedQuests = completed.filter{it.assignedTo == currentChild.id}.size
    val currentQuests = inProgress.filter{it.assignedTo == currentChild.id}.size
    Card(
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 16.dp,
                bottom = 8.dp,
                end = 16.dp)
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Row(
                modifier = Modifier,
            ){
                IconButton(
                    onClick = {
                        currentChildIndex = (currentChildIndex - 1 + children.size ) % children.size
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous"
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = "Child avatar",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
                IconButton(
                    onClick = {
                        currentChildIndex = (currentChildIndex + 1) % children.size
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next"
                    )
                }
            }
        }
    }
    Card(
        modifier = Modifier
            .padding(
                start = 16.dp,
                bottom = 8.dp,
                end = 16.dp)
            .fillMaxWidth()
            .height(50.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentChild.firstname,
                modifier = Modifier,
                fontSize = 32.sp
            )
        }
    }
    Card(
        modifier = Modifier
            .padding(start = 16.dp, bottom = 8.dp ,end = 16.dp)
            .fillMaxWidth()

    ){
        Column(
            modifier = Modifier
                .padding(8.dp)
        ){
            Text(
                "Quests in Progress: " + currentQuests,
                modifier = Modifier,
                fontSize = 16.sp
            )
            Text(
                "Quests Completed: " + completedQuests,
                modifier = Modifier,
                fontSize = 16.sp
            )
            Text(
                "Longest Quest Streak: ",
                modifier = Modifier,
                fontSize = 16.sp
            )
            Text(
                "Achievements: ",
                modifier = Modifier,
                fontSize = 16.sp
            )
            Text(
                "Total XP: ",
                modifier = Modifier,
                fontSize = 16.sp
            )
        }
    }
}