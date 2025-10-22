package com.example.rpg.ui.child.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.theme.RPGTheme


@Composable
fun ChildSettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ChildSettingsViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.padding(top = 85.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Child Settings Screen"
        )
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.signOut()
            navController.navigate(Routes.SignInScreen.route) {
                popUpTo(0) { inclusive = true }
            }
        }) {
            Text(text = "Sign Out")
        }
    }
}


@Preview
@Composable
fun PreviewChildSettingsScreen() {
    RPGTheme {
        ChildSettingsScreen(
            navController = rememberNavController(),
            overlayNavController = rememberNavController()
        )
    }
}