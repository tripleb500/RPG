package com.example.rpg.ui.child.game
// TODO: Implement screen
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

    LaunchedEffect(Unit) {
        viewModel.isValid()
    }

    // Try launching the Unity game once
    // Display UI based on the launch result
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        when {
            !valid -> {
                Text("You have reached the screen time limit")
            }

            launchStatus == LaunchStatus.Idle -> {
                Text("Launching game...")

                LaunchedEffect(user, level) {
                    if (user == null || level == null) return@LaunchedEffect  // wait until both are ready

                    val packageName = "com.DefaultCompany.clickerTypeBeat"
                    val activityName = "com.unity3d.player.UnityPlayerGameActivity"

                    val intent = Intent().apply {
                        setClassName(packageName, activityName)
                        putExtra("userName", user!!.username)
                        putExtra("userLevel", level)
                    }

                    try {
                        context.startActivity(intent)
                        launchStatus = LaunchStatus.Success
                    } catch (e: Exception) {
                        launchStatus = LaunchStatus.Failed
                    }
                }
            }

            launchStatus == LaunchStatus.Failed -> {
                Text("Error, please install the game")
            }

            launchStatus == LaunchStatus.Success -> {
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