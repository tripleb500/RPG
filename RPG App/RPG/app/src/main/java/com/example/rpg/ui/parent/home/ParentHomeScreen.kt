package com.example.rpg.ui.parent.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.rpg.R
import com.example.rpg.data.model.User
import com.example.rpg.ui.auth.AuthViewModel
import com.example.rpg.ui.child.home.ProfilePictureDialog
import com.example.rpg.ui.parent.addchild.ParentAddChildDialog


//In screens with a NavBar that need to be able to navigate to both types of screens (with and without bar)
//you need to add both navController and overlayNavController to the parameters

//Displays each family member below the image of the parent
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ParentHomeScreenViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val children by viewModel.children.collectAsState()
    val parentId = authViewModel.currentUser?.uid

    val user by viewModel.currentUserFlow.collectAsState(initial = null)

    var showDialog by remember { mutableStateOf(false) }

    var showProfilePictureDialog by remember { mutableStateOf(false) }

    LaunchedEffect(parentId) {
        if (parentId != null) {
            viewModel.loadChildren(parentId)
        }
    }

    Scaffold(
        // Top bar displays "Home". Informs user where they are.
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B2631)
                ),

                title = {
                    Text(
                        "Home",
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                    )
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Child")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 85.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = user?.profilePicture?.takeIf { it.isNotBlank() } ?: null,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(80.dp)
                    .clip(CircleShape) // This makes it a perfect circle
                    .clickable { showProfilePictureDialog = true },
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.baseline_person_24),
                error = painterResource(id = R.drawable.baseline_person_24)
            )

            LazyColumn {
                items(children, key = { it.id }) { child ->
                    CardView(child)
                }
            }
        }

//        if (showDialog && parentId != null) {
//            ParentAddChildDialog(
//                onDismissRequest = { showDialog = false }
//            )
//        }
        // Show dialog if FAB pressed
        if (showDialog) {
            ParentAddChildDialog(
                onDismissRequest = { showDialog = false },
                viewModel = viewModel,        // now the home screen ViewModel has addChildByUsername
                authViewModel = authViewModel
            )
        }

        if (showProfilePictureDialog) {
            ParentProfilePictureDialog(
                onDismissRequest = { showProfilePictureDialog = false },
                parentId = parentId
            )
        }
    }
}


//This function takes the members information and displays it in card view
@Composable
fun CardView(user: User) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user?.profilePicture?.takeIf { it.isNotBlank() } ?: null,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.baseline_person_24),
                error = painterResource(id = R.drawable.baseline_person_24)
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = "${user.firstname} ${user.lastname}",
                    style = MaterialTheme.typography.titleMedium
                )

                // TODO new repository
//                ProgressIndicator(
//                    progress = user.lvlProgress
//                )

            }
        }
    }
}

//Progress bar displays current level progress
@Composable
fun ProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier,
    )
}
