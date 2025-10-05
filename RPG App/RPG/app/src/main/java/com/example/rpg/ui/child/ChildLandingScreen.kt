package com.example.rpg.ui.child

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.R
import com.example.rpg.ui.Routes
import com.example.rpg.ui.parent.ParentLandingScreen
import com.example.rpg.ui.theme.RPGTheme

@Composable
fun ChildLandingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            //color tuple needs to be updated once material theming implemented
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF798B6A), Color(0xFF6c7d5f))
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 48.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                //switch button
                Button(
                    onClick = { navController.navigate(Routes.ParentLandingScreen.route) }) {
                    Text(text = "Child")
                }
            }
            //logo
            Image(
                painter = painterResource(R.drawable.rpg_logo_child),
                contentDescription = "ChildLandingScreen.kt logo",
                modifier = modifier
                    .padding(top = 128.dp)
                    .size(320.dp)
            )
            //play button
            Image(
                painter = painterResource(R.drawable.play_button_child),
                contentDescription = "Play button",
                modifier = modifier.padding(top = 64.dp)
                    .size(128.dp)
                    .clickable {
                        navController.navigate(Routes.ChildHomeScreen.route)
                    }
            )
        }
    }
}


@Preview
@Composable
fun PreviewChildLandingScreen(){
    RPGTheme {
        ChildLandingScreen(navController = rememberNavController())
    }
}
