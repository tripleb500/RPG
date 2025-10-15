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
import com.example.rpg.ui.parent.home.ParentHomeScreen

//ParentNavGraph handles navigation between screens with the parent version of a bottom NavBar
@Composable
fun ParentNavGraph(navController: NavHostController) {
    val overlayNavController = rememberNavController()
//Scaffold holding navGraph of screens with bottom bars and the actual bottom bar
    Scaffold(
        bottomBar = { ParentBottomBar(overlayNavController) }
    ) { innerPadding ->
        NavHost(
            navController = overlayNavController,
            startDestination = Routes.ParentHomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Parent Routes : parentHome
            composable(Routes.ParentHomeScreen.route) {
                ParentHomeScreen(navController = navController, overlayNavController = overlayNavController)
            }
            //testing purposes delete/swap out with parent screens later:
            //create ChildNavBarOverlay next

//            // Child Routes : childHome
//            composable(Routes.ChildHomeScreen.route) {
//                ChildHomeScreen(navController = navController, overlayNavController = overlayNavController)
//            }
        }
    }
}