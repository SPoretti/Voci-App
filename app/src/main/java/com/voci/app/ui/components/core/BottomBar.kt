package com.voci.app.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.voci.app.ui.navigation.currentRoute

@Composable
fun BottomBar(
    navController: NavHostController        // Navigation controller to navigate between screens
) {
    //------ Region: Data Initialization -----
    // List of screens to display in the bottom bar
    val items = listOf(
        Screens.Home,
        Screens.Requests
    )
    // Get the current route to highlight the selected item in the bottom bar
    val currentRoute = currentRoute(navController)

    //------ Region: View Composition -----
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        // Loop through the list of screens to create a NavigationBarItem for each
        items.forEach { screen ->
            NavigationBarItem(
                modifier = Modifier.hapticFeedback(),
                // Icon
                icon = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            )
                            .padding(8.dp)
                    ){
                        Icon(
                            screen.icon,
                            contentDescription = screen.title,
                        )
                    }
                },
                // Label
                label = {
                    Text(screen.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight =
                            if (currentRoute == screen.route)
                                FontWeight.ExtraBold
                            else
                                FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    )
                },
                // Highlight the selected item in the bottom bar
                selected = currentRoute == screen.route,
                onClick = {
                    if(currentRoute != screen.route){
                        navController.navigate(screen.route)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}