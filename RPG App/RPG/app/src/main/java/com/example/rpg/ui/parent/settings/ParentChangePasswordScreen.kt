package com.example.rpg.ui.parent.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
fun ParentChangePasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    overlayNavController: NavController,
    viewModel: ParentChangePasswordViewModel = hiltViewModel()
) {
    val currentPassword by viewModel.currentPassword
    val newPassword by viewModel.newPassword
    val confirmNewPassword by viewModel.confirmNewPassword
    val error by viewModel.errorMessage
    val success by viewModel.success
    val isLoading by viewModel.isLoading
    val context = LocalContext.current

    LaunchedEffect(success) {
        if (success) {
            Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
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
                        "Change Password",
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )
                }
            )
        }
    ) { paddingValues ->

        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // User's Current Password field
            OutlinedTextField(
                value = currentPassword,
                onValueChange = viewModel::onCurrentPasswordChange,
                label = { Text("Current Password") },
                singleLine = true
            )

            // User's new password field
            OutlinedTextField(
                value = newPassword,
                onValueChange = viewModel::onNewPasswordChange,
                label = { Text("New Password") },
                singleLine = true
            )

            // Confirm new password field
            OutlinedTextField(
                value = confirmNewPassword,
                onValueChange = viewModel::onConfirmNewPasswordChange,
                label = { Text("Confirm New Password") },
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
                Button(onClick = {overlayNavController.navigate(Routes.ParentAccountSettingsScreen.route)}
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = { viewModel.updatePassword() },
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