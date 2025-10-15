package com.example.rpg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.child.home.ChildHomeScreen
import com.example.rpg.ui.child.landing.ChildLandingScreen
import com.example.rpg.ui.parent.landing.ParentLandingScreen
import com.example.rpg.ui.signin.SignInScreen
import com.example.rpg.ui.theme.RPGTheme

//RPGNavGraph handles navigation between screens without NavBar
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

        // Parent Routes : parentNavBarOverlay
        composable(Routes.ParentNavGraph.route) {
            ParentNavGraph(navController = navController)
        }
        // Child Routes : childNavBarOverlay
        composable(Routes.ChildNavGraph.route) {
            ChildNavGraph(navController = navController)
        }

        // Child Routes : childHome
//        composable(Routes.ChildHomeScreen.route) {
//            ChildHomeScreen(navController = navController, overlayNavController = navController)
//        }
    }
}

@Preview
@Composable
fun PreviewScreenMain(){
    RPGTheme {
        RPGNavGraph()
    }
}