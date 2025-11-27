package com.example.rpg.ui.child.social

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.theme.RPGTheme

@Composable
fun ChildSocialScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ChildSocialViewModel = hiltViewModel()
) {
    val friendsLeaderboard by viewModel.friendsLeaderboard.collectAsState()
    val currentUserId by viewModel.currentUserIdFlow.collectAsState(initial = null)
    var showAddFriendDialog by remember { mutableStateOf(false) }
    var usernameInput by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Leaderboard",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(friendsLeaderboard) { userWithStats ->
                    val user = userWithStats.first
                    val stats = userWithStats.second
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(
                                color = if (user.id == currentUserId) Color(0xFFBBDEFB)
                                else Color(0xFFEFEFEF),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = user.username,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "XP: ${stats.totalXP}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Floating button
        FloatingActionButton(
            onClick = { showAddFriendDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = "Add Friend")
        }
    }

    // Add Friend Dialog
    if (showAddFriendDialog) {
        AlertDialog(
            onDismissRequest = { showAddFriendDialog = false },
            title = { Text("Add Friend") },
            text = {
                TextField(
                    value = usernameInput,
                    onValueChange = { usernameInput = it },
                    placeholder = { Text("Enter friend's username") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val username = usernameInput.trim()

                        if (username.isNotBlank()) {
                            viewModel.addFriendByUsername(
                                username = username,
                                onSuccess = {
                                    usernameInput = ""
                                    showAddFriendDialog = false
                                }
                            )
                        }
                    },
                    enabled = usernameInput.isNotBlank() && !viewModel.isLoadingFriends
                ) {
                    Text(if (viewModel.isLoadingFriends) "Adding..." else "Add")
                }
            },
            dismissButton = {
                Button(onClick = { showAddFriendDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewChildSocialScreen() {
    RPGTheme {
        ChildSocialScreen(
            navController = rememberNavController(),
            overlayNavController = rememberNavController()
        )
    }
}