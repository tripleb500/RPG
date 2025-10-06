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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
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
            //color tuple needs to be updated once material theming implemented
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF4782B2), Color(0xFF5F7F93))
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
                    onClick = { navController.navigate(Routes.ChildLandingScreen.route) },
                    modifier = Modifier.size(width = 200.dp, height = 60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        //background
                        containerColor = Color(0xFF7EB3F5),
                        //text
                        contentColor = Color.White)
                ) {
                    Text(
                        text = "Parent",
                        fontSize = 24.sp
                    ) }
            }
            //logo
            Image(
                painter = painterResource(R.drawable.rpg_logo_parent),
                contentDescription = "ParentLandingScreen.kt logo",
                modifier = modifier
                    .padding(top = 128.dp)
                    .size(320.dp)
            )
            //play button
            //reroute from ChildHomeScreen.kt to parent equivalent once implemented)
            Image(
                painter = painterResource(R.drawable.play_button_parent),
                contentDescription = "Play button",
                modifier = modifier.padding(top = 64.dp)
                    .size(128.dp)
                    .clickable {
                        navController.navigate(Routes.ParentHomeScreen.route)
                    }
            )
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
