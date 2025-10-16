package com.example.rpg.ui.parent.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.theme.RPGTheme

@Composable
fun ParentSettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ParentSettingsViewModel = viewModel()
) {
    Column(
        modifier = Modifier.padding(top = 85.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Parent Settings Screen"
        )
    }
}

@Preview
@Composable
fun PreviewParentQuestScreen(){
    RPGTheme {
        ParentSettingsScreen(navController = rememberNavController(), overlayNavController = rememberNavController())
    }
}