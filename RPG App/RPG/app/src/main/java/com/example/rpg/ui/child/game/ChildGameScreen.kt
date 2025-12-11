package com.example.rpg.ui.child.game
// TODO: Implement screen
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun ChildGameScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ChildGameViewModel = hiltViewModel()
) {
    val user by viewModel.currentUserFlow.collectAsState(initial = null)
    val level by viewModel.currentLevel.collectAsState(initial = null)
    val context = LocalContext.current
    var launchStatus by remember { mutableStateOf<LaunchStatus>(LaunchStatus.Idle) }

    val valid by viewModel.valid.collectAsState()

    // Try launching the Unity game once
    LaunchedEffect(Unit) {
        viewModel.isValid()
        val intent = Intent().apply {
            setClassName(
                "com.DefaultCompany.clickerTypeBeat",
                "com.unity3d.player.UnityPlayerGameActivity"
            )
        }
        launchStatus = try {
            context.startActivity(intent)
            LaunchStatus.Success
        } catch (e: Exception) {
            e.printStackTrace()
            LaunchStatus.Failed
        }
    }
    // Display UI based on the launch result
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (viewModel.isValid())
            Text("You have reached the screen time limit")
        else {
            // Try launching the Unity game once
            LaunchedEffect(user, level) {
                val u = user
                val userLvl = level
                if (u == null || userLvl == null) return@LaunchedEffect  // wait until both are ready

                val userName = u.username
                val intent = Intent().apply {
                    setClassName(
                        "com.DefaultCompany.clickerTypeBeat",
                        "com.unity3d.player.UnityPlayerGameActivity"
                    )

                }
                intent.putExtra("userName",userName)
                intent.putExtra("userLevel", userLvl)
                launchStatus = try {
                    context.startActivity(intent)
                    LaunchStatus.Success
                } catch (e: Exception) {
                    e.printStackTrace()
                    LaunchStatus.Failed
                }
            }

    // Display UI based on the launch result
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (!valid)
            Text("You have reached the screen time limit")
        else {
            when (launchStatus) {
                LaunchStatus.Idle -> {
                    Text(
                        text = "Launching Unity game...",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                LaunchStatus.Success -> {
                    Text(
                        text = "Unity game has been launched! Return here to see this message.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Green,
                        textAlign = TextAlign.Center
                    )
                }

                LaunchStatus.Failed -> {
                    Text(
                        text = "Unity game has not been installed.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// Sealed class for launch state
sealed class LaunchStatus {
    object Idle : LaunchStatus()
    object Success : LaunchStatus()
    object Failed : LaunchStatus()
}