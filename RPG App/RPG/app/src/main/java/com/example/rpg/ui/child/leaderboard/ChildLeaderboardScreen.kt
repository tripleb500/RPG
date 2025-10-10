package com.example.rpg.ui.child.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.theme.RPGTheme

@Composable
fun ChildLeaderboardScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = Modifier.padding(top = 85.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}

@Preview
@Composable
fun PreviewChildLeaderboardScreen(){
    RPGTheme {
        ChildLeaderboardScreen(navController = rememberNavController())
    }
}