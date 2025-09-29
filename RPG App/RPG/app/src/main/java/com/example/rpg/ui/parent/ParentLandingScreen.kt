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
import com.example.rpg.ui.Routes

@Composable
fun ParentLandingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
        .padding(top = 85.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Parent",
            modifier = modifier.padding(24.dp)
        )
        //switch button
        Button(
            modifier = Modifier.padding(top = 85.dp),
            onClick = { navController.navigate(Routes.ChildLandingScreen.route) }) {
            Text(text = "Parent")
        }
    }
}