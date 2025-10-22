package com.example.rpg.ui.child.home
// TODO: button for stats; achievements
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.R
<<<<<<< Updated upstream
import com.example.rpg.ui.Routes
import com.example.rpg.ui.child.quest.Quest
=======
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.child.achievements.ChildAchievementsDialog
>>>>>>> Stashed changes
import com.example.rpg.ui.parent.home.Family
import com.example.rpg.ui.parent.home.ProgressIndicator
import com.example.rpg.ui.theme.RPGTheme

val child = Family("Bradford", 1, 0.1F)

@Composable
fun ChildHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ChildHomeViewModel = viewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    ) {
    var showDialog by remember { mutableStateOf(false) }
    val childQuestList = viewModel.quests
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
        Row {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "Photo of Avatar",
                modifier = Modifier.padding(top = 45.dp)
                    .width(100.dp)
                    .height(100.dp)
            )
            Column(
                modifier = Modifier.padding(top = 50.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = child.childName,
                    modifier = Modifier.padding(top = 16.dp, bottom = 20.dp)
                )

                ProgressIndicator(
                    progress = child.lvlProgress
                )
            }
        }

        Column(
            modifier = Modifier.padding(top = 85.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
<<<<<<< Updated upstream
            //switch button
            Button(
                modifier = Modifier.padding(top = 85.dp),
                onClick = { navController.navigate(Routes.ChildLandingScreen.route) }) {
                Text(text = "Landing Page")
            }

            LazyColumn {
                items(childQuestList) { quest ->
                    CardView(quest)
                }
            }
=======
            // Entire card is now the tap target
            Card(
                modifier = Modifier.padding(48.dp),
                onClick = { showDialog = true }, // open dialog on card tap
            ) {
                Row(Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.outline_photo_camera_back_24),
                        contentDescription = "Photo of quest",
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    )
                    Column(Modifier.padding(start = 16.dp)) {
                        Text(
                            text = "Achievements",
                        )
                    }
                }
            }
            if (showDialog) {
                ChildAchievementsDialog(
                    onDismissRequest = { showDialog = false },
                    viewModel = viewModel,
                    authViewModel = authViewModel
                )
            }
>>>>>>> Stashed changes
        }
    }
}
/*@Composable
fun CardView(quest: Quest) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        var showDialog by remember { mutableStateOf(false) }
        Row {
            Image(
                painter = painterResource(id = R.drawable.outline_photo_camera_back_24),
                contentDescription = "Photo of quest",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Column {
<<<<<<< Updated upstream
                Text(
                    text = quest.questName,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Reward: " + quest.Reward,
                )
                Text(
                    text = "Due: " + quest.deadline,
                )
=======
                Button(
                    onClick = { showDialog = true }
                ) {
                    Text(text = "Landing Page",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(top = 16.dp))
                }

>>>>>>> Stashed changes
            }
        }
    }
    if (showDialog) {
        ChildAchievementsDialog(
            onDismissRequest = { showDialog = false },
            viewModel = viewModel,        // now the home screen ViewModel has addChildByUsername
            authViewModel = authViewModel
        )
    }
}*/

@Composable
fun ProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier
){
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier,
    )
}

// Show dialog if FAB pressed


@Preview
@Composable
fun PreviewChildHomeScreen(){
    RPGTheme {
        ChildHomeScreen(navController = rememberNavController(), overlayNavController = rememberNavController())
    }
}
