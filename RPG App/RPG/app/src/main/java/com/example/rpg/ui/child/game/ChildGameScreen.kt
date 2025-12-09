package com.example.rpg.ui.child.game
// TODO: Implement screen
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController

@Composable
fun ChildGameScreen(
    navController: NavHostController,
    overlayNavController: NavHostController
) {
    val context = LocalContext.current
    var launchStatus by remember { mutableStateOf<LaunchStatus>(LaunchStatus.Idle) }

    // Try launching the Unity game once
    LaunchedEffect(Unit) {
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

// Sealed class for launch state
sealed class LaunchStatus {
    object Idle : LaunchStatus()
    object Success : LaunchStatus()
    object Failed : LaunchStatus()
}