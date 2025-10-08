package com.example.rpg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.child.ChildHomeScreen
import com.example.rpg.ui.child.ChildLandingScreen
import com.example.rpg.ui.parent.ParentHomeScreen
import com.example.rpg.ui.parent.ParentLandingScreen
import com.example.rpg.ui.parent.ParentNavBarOverlay
import com.example.rpg.ui.signin.SignInScreen
import com.example.rpg.ui.theme.RPGTheme

@Composable
fun RPGNavGraph() {
    val navController = rememberNavController()
    // Start Destination: parentLandingScreen
    NavHost(navController = navController, startDestination = Routes.ParentLandingScreen.route) {
        // Account Management Route : signInScreen
        composable(Routes.SignInScreen.route) {
            SignInScreen(navController = navController)
        }
        // Landing Page Route : parentLanding
        composable(Routes.ParentLandingScreen.route) {
            ParentLandingScreen(navController = navController)
        }
        // Landing Page Route : childLanding
        composable(Routes.ChildLandingScreen.route) {
            ChildLandingScreen(navController = navController)
        }
        // Child Routes : childHome
        composable(Routes.ChildHomeScreen.route) {
            ChildHomeScreen(navController = navController)
        }
        // Parent Routes : parentHome
        composable(Routes.ParentHomeScreen.route) {
            ParentHomeScreen(navController = navController)
        }
        // Parent Routes : parentNavBarOverlay
        composable(Routes.ParentNavBarOverlay.route) {
            ParentNavBarOverlay()
        }
    }
}

@Preview
@Composable
fun PreviewScreenMain(){
    RPGTheme {
        RPGNavGraph()
    }
}