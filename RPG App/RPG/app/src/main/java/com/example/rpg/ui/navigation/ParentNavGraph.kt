package com.example.rpg.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.parent.ParentBottomBar
import com.example.rpg.ui.parent.addquest.ParentAddQuestScreen
import com.example.rpg.ui.parent.home.ParentHomeScreen
import com.example.rpg.ui.parent.quest.ParentQuestScreen
import com.example.rpg.ui.parent.settings.ParentSettingsScreen
import com.example.rpg.ui.parent.stats.ParentStatsScreen
import com.example.rpg.ui.parent.settings.ParentAccountSettingsScreen
import com.example.rpg.ui.parent.settings.ParentChangeEmailScreen
import com.example.rpg.ui.parent.settings.ParentChangePasswordScreen

//ParentNavGraph handles navigation between screens with the parent version of a bottom NavBar
@Composable
fun ParentNavGraph(navController: NavHostController) {
    val overlayNavController = rememberNavController()
    // Scaffold holding navGraph of screens with bottom bars and the actual bottom bar
    Scaffold(
        bottomBar = { ParentBottomBar(overlayNavController) }
    ) { innerPadding ->
        NavHost(
            navController = overlayNavController,
            startDestination = Routes.ParentHomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Parent Routes : ParentQuest
            composable(Routes.ParentQuestScreen.route) {
                ParentQuestScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            // Parent Routes : ParentAddQuest
            composable(Routes.ParentAddQuestScreen.route) {
                ParentAddQuestScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            // Parent Routes : ParentHome
            composable(Routes.ParentHomeScreen.route) {
                ParentHomeScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            // Parent Routes : ParentStats
            composable(Routes.ParentStatsScreen.route) {
                ParentStatsScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            // Parent Routes : ParentSettings
            composable(Routes.ParentSettingsScreen.route) {
                ParentSettingsScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            composable(Routes.ParentAccountSettingsScreen.route) {
                ParentAccountSettingsScreen(
                    navController = navController,
                    overlayNavController = overlayNavController,
                )
            }
            composable (Routes.ParentChangeEmailScreen.route) {
                ParentChangeEmailScreen(
                    navController = navController,
                    overlayNavController = overlayNavController,
                )
            }
            composable (Routes.ParentChangePasswordScreen.route) {
                ParentChangePasswordScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

        }
    }
}