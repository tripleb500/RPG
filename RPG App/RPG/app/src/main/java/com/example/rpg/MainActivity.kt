package com.example.rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rpg.ui.navigation.RPGNavGraph
import com.example.rpg.ui.theme.RPGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RPGTheme {
                //SignUpScreen() //Used to quickly check textfield and radio button
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
