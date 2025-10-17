package com.example.rpg.ui.parent

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rpg.data.model.ParentNavBar


@Composable
fun ParentBottomBar(overlayNavController: NavHostController) {
    val backStackEntry by overlayNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        ParentNavBar.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute?.endsWith(item.route) == true,
                onClick = {
                    //if dialog don't reset backstack
                    if (item == ParentNavBar.ParentAdd) {
                        overlayNavController.navigate(item.route) {
                            launchSingleTop = true
                        }
                    }
                    else {
                        //normal
                        overlayNavController.navigate(item.route) {
                            popUpTo(overlayNavController.graph.startDestinationId)
                        }
                    }
                },
                icon = { Icon(painterResource(item.icon), contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}
