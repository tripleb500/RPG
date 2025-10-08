package com.example.rpg.ui.parent

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rpg.data.model.ParentNavBar
import com.example.rpg.ui.Routes
import com.example.rpg.ui.child.ChildHomeScreen

@Composable
fun ParentNavBarOverlay() {
    val overlayNavController = rememberNavController()

    Scaffold(
        bottomBar = { ParentBottomBar(overlayNavController) }
    ) { innerPadding ->
        NavHost(
            navController = overlayNavController,
            startDestination = Routes.ParentHomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.ParentHomeScreen.route) { ParentHomeScreen(navController = overlayNavController) }
            //testing purposes delete later:
            composable(Routes.ChildHomeScreen.route) { ChildHomeScreen(navController = overlayNavController) }
            //composable(Routes.ParentHomeScreen.route) { ParentHomeScreen(navController = navController) }
            //composable(Routes.ParentHomeScreen.route) { ParentHomeScreen(navController = navController) }

        }
    }
}

@Composable
private fun ParentBottomBar(overlayNavController: NavHostController) {
    val backStackEntry by overlayNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        ParentNavBar.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute?.endsWith(item.route.toString()) == true,
                onClick = {
                    overlayNavController.navigate(item.route) {
                        popUpTo(overlayNavController.graph.startDestinationId)
                    }
                },
                icon = { Icon(painterResource(item.icon), contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}
