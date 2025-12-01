package com.example.rpg.ui.child.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.theme.RPGTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildSettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController
) {
    val settingsOptions = listOf(
        1 to "Account",
        2 to "Notifications",
        3 to "Privacy",
        4 to "Appearance",
        5 to "About",
        6 to "Logout"
    )
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A4A17)
                ),

                title = {
                    Text(
                        "Settings",
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                    )
                }
            )
        },
    ){ paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(settingsOptions) { (id, name) ->
                    ClickableCard(
                        title = name,
                        onClick = {
                            when (id) {
                                1 -> overlayNavController.navigate(Routes.ChildAccountSettingsScreen.route)
                                2 -> overlayNavController.navigate(Routes.ChildNotificationsScreen.route)
                                6 -> {
                                    navController.navigate(Routes.SignInScreen.route) {
                                        popUpTo(id = 0) {inclusive = true}
                                    }
                                }
                            }

                            //viewModel.onCardClicked(id)
                            //navController.navigate(Routes.SignInScreen.route) {
                            //popUpTo(0) { inclusive = true }
                            //}
                        })
                }
            }
        }
    }
}

@Composable
fun ClickableCard(
    title: String,
    subtitle: String? = null,
    iconRes: Int? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .padding(12.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(iconRes != null) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
            }
            /**
            Image(
            painter = painterResource(id = R.drawable.baseline_person_24),
            contentDescription = "Photo of default avatar",
            modifier = Modifier
            .width(40.dp)
            .height(40.dp)
            )
             */
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                subtitle?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End
                    )
                }
            }

            //Spacer(modifier = Modifier.width(16.dp))

            //Text(
            //text = title, textAlign = TextAlign.Center
            //)
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