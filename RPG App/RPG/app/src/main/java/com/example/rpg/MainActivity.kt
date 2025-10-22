package com.example.rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rpg.ui.navigation.RPGNavGraph
import com.example.rpg.ui.theme.RPGTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RPGTheme {
                RPGNavGraph()
            }
        }
    }
}

@Preview
@Composable
fun PreviewLandingPage() {
    RPGTheme {
        RPGNavGraph()
    }
}
