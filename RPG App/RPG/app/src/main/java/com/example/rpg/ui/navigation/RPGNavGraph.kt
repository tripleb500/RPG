package com.example.rpg.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.auth.AuthState
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.auth.SplashScreen
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
fun RPGNavGraph(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState(initial = AuthState.Loading)
    // Start Destination: parentLandingScreen
    NavHost(navController = navController, startDestination = Routes.SplashScreen.route) {
        composable(Routes.SplashScreen.route) {
            //val authState by authViewModel.authState.collectAsState()

            LaunchedEffect(authState) {
                when(authState) {
                    is AuthState.Authenticated -> {
                        val user = (authState as AuthState.Authenticated).user
                        val destination = when (user.familyRole) {
                            "parent" -> Routes.ParentHomeScreen.route
                            "child" -> Routes.ChildHomeScreen.route
                            else -> Routes.SignInScreen.route
                        }
                        navController.navigate(destination) {
                            popUpTo(Routes.SplashScreen.route) {inclusive = true}
                        }
                    }
                    AuthState.Unauthenticated -> {
                        navController.navigate(Routes.SignInScreen.route) {
                            popUpTo(Routes.SplashScreen.route) {inclusive = true}
                        }
                    }
                    is AuthState.Error -> {
                        navController.navigate(Routes.SignInScreen.route) {
                            popUpTo(Routes.SplashScreen.route) {inclusive = true}
                        }
                    }
                    AuthState.Loading -> {}
                }
            }
            SplashScreen()
        }
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
    }
}