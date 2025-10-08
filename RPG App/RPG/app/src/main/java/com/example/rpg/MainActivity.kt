package com.example.rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.child.ChildHomeScreen
import com.example.rpg.ui.child.ChildLandingScreen
import com.example.rpg.ui.parent.ParentHomeScreen
import com.example.rpg.ui.parent.ParentLandingScreen
import com.example.rpg.ui.theme.RPGTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RPGTheme {
<<<<<<< Updated upstream
                ScreenMain()
=======
                //SignUpScreen() //Used to quickly check textfield and radio button
                //ScreenMain()
>>>>>>> Stashed changes
            }
        }
    }
}

//move this to another file after done testing
@Composable
fun ScreenMain() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.ParentLandingScreen.route) {
// First route : parentLanding
        composable(Routes.ParentLandingScreen.route) {
            ParentLandingScreen(navController = navController)
        }
// Another Route : childLanding
        composable(Routes.ChildLandingScreen.route) {
            ChildLandingScreen(navController = navController)
        }
// Child Route : childHome
        composable(Routes.ChildHomeScreen.route) {
            ChildHomeScreen(navController = navController)
        }
// Parent Route: parentHome
        composable(Routes.ParentHomeScreen.route) {
            ParentHomeScreen(navController = navController)
        }
    }
}

@Preview
@Composable
fun PreviewScreenMain(){
    RPGTheme {
            ScreenMain()
    }
}
