package com.example.vociapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.navigation.currentRoute

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        Screens.Home,
        Screens.Requests,
        Screens.UserProfile,
        //Screens.
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
                        /*
                        by removing this we are able to navigate to the main screen
                        i.e. if I go to the requests history screen and then tap on the
                        requests item in the bottom bar it will navigate to the requests screen


                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true*/
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }
}