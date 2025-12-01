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
import com.example.rpg.ui.child.ChildBottomBar
import com.example.rpg.ui.child.game.ChildGameScreen
import com.example.rpg.ui.child.home.ChildHomeScreen
import com.example.rpg.ui.child.quest.ChildQuestScreen
import com.example.rpg.ui.child.settings.ChildSettingsScreen
import com.example.rpg.ui.child.social.ChildSocialScreen
import com.example.rpg.ui.child.quest.ChildCameraScreen
import com.example.rpg.ui.child.settings.ChildNotificationsScreen
import com.example.rpg.ui.child.settings.ChildAccountSettingsScreen
import com.example.rpg.ui.child.settings.ChildChangeEmailScreen
import com.example.rpg.ui.child.settings.ChildChangePasswordScreen
import com.example.rpg.ui.child.settings.ChildChangeUsernameScreen

//ChildNavGraph handles navigation between screens with the Child version of a bottom NavBar
@Composable
fun ChildNavGraph(navController: NavHostController) {
    val overlayNavController = rememberNavController()
//Scaffold holding navGraph of screens with bottom bars and the actual bottom bar
    Scaffold(
        bottomBar = { ChildBottomBar(overlayNavController) }
    ) { innerPadding ->
        NavHost(
            navController = overlayNavController,
            startDestination = Routes.ChildHomeScreen.route,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            // Child Routes : ChildGame
            composable(Routes.ChildGameScreen.route) {
                ChildGameScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            // Child Routes : ChildQuest
            composable(Routes.ChildQuestScreen.route) {
                ChildQuestScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }


            // Child Routes : ChildHome
            composable(Routes.ChildHomeScreen.route) {
                ChildHomeScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            // Child Routes : ChildSocial
            composable(Routes.ChildSocialScreen.route) {
                ChildSocialScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            // Child Routes : ChildSettings
            composable(Routes.ChildSettingsScreen.route) {
                ChildSettingsScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            composable(Routes.ChildAccountSettingsScreen.route) {
                ChildAccountSettingsScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            composable(Routes.ChildNotificationsScreen.route) {
                ChildNotificationsScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            composable(Routes.ChildChangeUsernameScreen.route) {
                ChildChangeUsernameScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            composable(Routes.ChildChangeEmailScreen.route) {
                ChildChangeEmailScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }

            composable(Routes.ChildChangePasswordScreen.route){
                ChildChangePasswordScreen(
                    navController = navController,
                    overlayNavController = overlayNavController
                )
            }
        }
    }
}