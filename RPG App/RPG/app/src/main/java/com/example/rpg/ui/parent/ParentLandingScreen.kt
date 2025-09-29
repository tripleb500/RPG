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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.rpg.R
import com.example.rpg.ui.Routes

@Composable
fun ParentLandingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Surface(
        modifier = Modifier
        .fillMaxSize()
            .background(brush = Brush.linearGradient(
                colors = listOf(Color.Cyan,Color.White),
                start = Offset(0f,0f),
                end = Offset(0f,0f)
            ))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                //switch button
                Button(
                    modifier = Modifier.padding(bottom = 85.dp),
                    onClick = { navController.navigate(Routes.ChildLandingScreen.route) }) {
                    Text(text = "Parent")
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 85.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //logo
                Image(
                    painter = painterResource(R.drawable.rpg_logo_parent),
                    contentDescription = null,
                    modifier = modifier.padding(24.dp)
                )
            }
        }
    }
}