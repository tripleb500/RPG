package com.example.rpg.ui.child.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rpg.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildChangeUsernameScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    overlayNavController: NavController,
    viewModel: ChildChangeUsernameViewModel = hiltViewModel()
) {
    val currentUsername by viewModel.currentUsername
    val newUsername by viewModel.newUsername
    val error by viewModel.errorMessage
    val isLoading by viewModel.isLoading
    val context = LocalContext.current
    val success by viewModel.success

    LaunchedEffect(success) {
        if (success) {
            Toast.makeText(context, "Username updated successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                colors  = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B2631)
                ),
                title = {
                    Text(
                        "Change Username",
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // User's current username displayed
            OutlinedTextField(
                value = currentUsername ?: "",
                onValueChange = {},
                label = { Text("Current Username") },
                enabled = false
            )

            // Change username field
            OutlinedTextField(
                value = newUsername,
                onValueChange = {viewModel.newUsername.value = it},
                label = { Text("New Username") },
                singleLine = true
            )

            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Row (
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {overlayNavController.navigate(Routes.ChildAccountSettingsScreen.route)}
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = { viewModel.updateUsername() },
                    enabled = !isLoading,
                ) {
                    if(isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Confirm")
                    }
                }
            }
        }
    }

}