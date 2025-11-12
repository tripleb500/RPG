package com.example.rpg.ui.child.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.parent.settings.ClickableCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildAccountSettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ChildAccountSettingsViewModel = hiltViewModel()
) {


    val username = viewModel.username.value ?: "Loading..."
    val userEmail = viewModel.userEmail.value ?: "Loading..."

    val accountSettings = listOf(
        1 to "Change username",
        2 to "Change email",
        3 to "Change password"
    )

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                colors  = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A4A17)
                ),
                title = {
                    Text(
                        "Account Settings",
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
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(accountSettings) {(id, name) ->

                    val subtitle = when(id) {
                        1 -> username
                        2 -> userEmail
                        else -> null
                    }

                    ClickableCard(
                        title = name,
                        subtitle = subtitle,
                        onClick = {
                            when (id) {
                                1 -> overlayNavController.navigate(Routes.ChildChangeUsernameScreen.route)
                                2 -> overlayNavController.navigate(Routes.ChildChangeEmailScreen.route)
                                3 -> overlayNavController.navigate(Routes.ChildChangePasswordScreen.route)
                            }
                        }
                    )
                }
            }
        }
    }

}