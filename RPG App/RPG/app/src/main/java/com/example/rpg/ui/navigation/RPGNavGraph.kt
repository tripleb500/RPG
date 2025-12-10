package com.example.rpg.ui.navigation

import ParentPaymentScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.auth.signin.SignInScreen
import com.example.rpg.ui.auth.signup.SignUpScreen
import com.example.rpg.ui.child.landing.ChildLandingScreen
import com.example.rpg.ui.parent.landing.ParentLandingScreen
import com.example.rpg.ui.parent.settings.ParentAccountSettingsScreen
import com.example.rpg.ui.parent.settings.ParentChangeEmailScreen
import com.example.rpg.ui.parent.settings.ParentChangePasswordScreen
import com.example.rpg.ui.parent.settings.ParentChangeUsernameScreen
import com.example.rpg.ui.parent.settings.ParentNotificationScreen
import com.example.rpg.ui.parent.settings.ParentSettingsScreen
import com.example.rpg.ui.theme.RPGTheme

//RPGNavGraph handles navigation between screens without NavBar
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RPGNavGraph() {
    val navController = rememberNavController()
    // Start Destination: parentLandingScreen
    NavHost(navController = navController, startDestination = Routes.SignInScreen.route) {
        // Account Management Route : signInScreen
        composable(Routes.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }
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

        composable(Routes.ParentSettingsScreen.route) {
            ParentSettingsScreen(navController = navController)
        }

        composable(Routes.ParentAccountSettingsScreen.route) {
            ParentAccountSettingsScreen(navController = navController)
        }

        composable(Routes.ParentNotificationsScreen.route) {
            ParentNotificationScreen(navController = navController)
        }

        composable(Routes.ParentChangeUsernameScreen.route) {
            ParentChangeUsernameScreen(navController = navController)
        }

        composable(Routes.ParentChangeEmailScreen.route) {
            ParentChangeEmailScreen(navController = navController)
        }

        composable(Routes.ParentChangePasswordScreen.route) {
            ParentChangePasswordScreen(navController = navController)
        }

        composable(Routes.ParentPaymentScreen.route) {
            ParentPaymentScreen(navController = navController)
        }
    }
}