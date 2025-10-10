package com.example.rpg.ui.child.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.R
import com.example.rpg.ui.theme.RPGTheme



@Composable
fun ChildProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = Modifier.padding(top = 85.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_person_24),
            contentDescription = "Photo  of Avatar",
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )
    }
}



@Preview
@Composable
fun PreviewChildProfileScreen(){
    RPGTheme {
        ChildProfileScreen(navController = rememberNavController())
    }
}