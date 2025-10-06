package com.example.rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rpg.ui.navigation.RPGNavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.child.ChildHomeScreen
import com.example.rpg.ui.child.ChildLandingScreen
import com.example.rpg.ui.parent.ParentHomeScreen
import com.example.rpg.ui.parent.ParentLandingScreen
import com.example.rpg.ui.signup.SignUpScreen
import com.example.rpg.ui.theme.RPGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RPGTheme {
                //SignUpScreen() Used to quickly check textfield and radio button
                RPGNavGraph()
            }
        }
    }
}

@Preview
@Composable
fun PreviewLandingPage(){
    RPGTheme {
        RPGNavGraph()
    }
}
