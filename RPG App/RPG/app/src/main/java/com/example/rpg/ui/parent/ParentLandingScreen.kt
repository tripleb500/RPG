package com.example.rpg.ui.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.rpg.R
import com.example.rpg.ScreenMain
import com.example.rpg.ui.Routes
import com.example.rpg.ui.theme.RPGTheme

@Composable
fun ParentLandingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF798B6A), Color(0xFF6c7d5f))
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 48.dp, bottom = 80.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                //switch button
                Button(
                    onClick = { navController.navigate(Routes.ChildLandingScreen.route) }) {
                    Text(text = "Parent")
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 48.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //logo
                Image(
                    painter = painterResource(R.drawable.rpg_logo_parent),
                    contentDescription = "ParentLandingScreen.kt logo",
                )
                //play button (Drawable place holder)
                //reroute from ChildHomeScreen.kt to parent equivalent once implemented)
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "Play button",
                    modifier = modifier.padding(bottom = 48.dp)
                        .size(2400.dp)
                        .clickable {
                            navController.navigate(Routes.ChildHomeScreen.route)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewParentLandingScreen(){
    RPGTheme {
        ParentLandingScreen(navController = rememberNavController())
    }
}
