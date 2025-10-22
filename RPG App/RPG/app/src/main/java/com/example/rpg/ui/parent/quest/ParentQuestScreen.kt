package com.example.rpg.ui.parent.quest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.parent.home.ParentHomeScreenViewModel
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
            LazyColumn {
                childQuestMap.value.forEach { (assigneeId, quests) ->
                    // Maybe display the child's name here using assigneeId
                    item {
                        Text("Quests for: $assigneeId")
                    }

                    items(quests) { quest ->
                        Text(quest.title)
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun PreviewParentQuestScreen(){
    RPGTheme {
        ParentQuestScreen(navController = rememberNavController(), overlayNavController = rememberNavController())
    }
}