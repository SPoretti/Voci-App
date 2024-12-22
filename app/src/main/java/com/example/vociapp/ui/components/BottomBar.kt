package com.example.vociapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
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
import com.example.vociapp.ui.components.utils.hapticFeedback
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.navigation.currentRoute

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        Screens.Home,
        Screens.Requests
    )
    val currentRoute = currentRoute(navController)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .offset(y = 0.dp)
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .hapticFeedback(),
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
                    )},
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route)
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