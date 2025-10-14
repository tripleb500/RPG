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
import com.example.rpg.ui.child.home.ChildHomeScreen
import com.example.rpg.ui.child.profile.ChildProfileScreen

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
            modifier = Modifier.padding(innerPadding)
        ) {
            // Child Routes : ChildHome
            composable(Routes.ChildHomeScreen.route) {
                ChildHomeScreen(navController = navController, overlayNavController = overlayNavController)
            }

            // Child Routes : ChildProfile
            composable(Routes.ChildProfileScreen.route) {
                ChildProfileScreen(navController = navController, overlayNavController = overlayNavController)
            }
            //testing purposes delete/swap out with Child screens later:
            //create ChildNavBarOverlay next

//            // Child Routes : childHome
//            composable(Routes.ChildHomeScreen.route) {
//                ChildHomeScreen(navController = navController, overlayNavController = overlayNavController)
//            }
        }
    }
}