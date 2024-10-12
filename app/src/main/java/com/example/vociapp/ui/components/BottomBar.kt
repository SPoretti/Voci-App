package com.example.vociapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.vociapp.ui.navigation.Screens
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import com.example.vociapp.ui.navigation.currentRoute

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        Screens.Home,
        Screens.UserProfile
    )
    val currentRoute = currentRoute(navController)

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}