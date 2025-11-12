package com.example.rpg.ui.child.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.navigation.NavHostController
import com.example.rpg.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildChangeEmailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    overlayNavController: NavHostController,
    viewModel: ChildChangeEmailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentEmail = viewModel.currentEmail.value
    val newEmail by viewModel.newEmail
    val confirmEmail by viewModel.confirmEmail
    val password by viewModel.password
    val error by viewModel.errorMessage
    val success by viewModel.success

    LaunchedEffect(success) {
        if (success) {
            Toast.makeText(context, "Email updated successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                colors  = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A4A17)
                ),
                title = {
                    Text(
                        "Change Email",
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

            // User Current Email
            OutlinedTextField(
                value = currentEmail ?: "",
                onValueChange = {},
                label = {Text ("Current Email") },
                enabled = false
            )

            // New Email Field
            OutlinedTextField(
                value = newEmail,
                onValueChange = {viewModel.newEmail.value = it },
                label = {Text("New Email") },
                singleLine = true,
            )

            Spacer(Modifier.height(8.dp))

            // Confirm Email Field
            OutlinedTextField(
                value = confirmEmail,
                onValueChange = {viewModel.confirmEmail.value = it },
                label = { Text("Confirm Email") },
                singleLine = true,
            )

            // Password Confirmation
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.password.value = it },
                label = { Text ("Current Password") },
                singleLine = true
            )

            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {overlayNavController.navigate(Routes.ChildAccountSettingsScreen.route)}
                ) {
                    Text("Cancel")
                }
                Button(onClick = { viewModel.updateEmail() }
                ) {
                    Text("Confirm")
                }
            }

        }

    }

}