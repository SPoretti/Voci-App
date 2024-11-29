package com.example.vociapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.navigation.currentRoute
import com.example.vociapp.ui.theme.ColorPalette

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        Screens.Home,
        Screens.Requests,
        Screens.UserProfile,
        //Screens.
    )
    val currentRoute = currentRoute(navController)

    NavigationBar(
        containerColor = ColorPalette.BackgroundNavBarColor,
        modifier = Modifier
            .padding(vertical = 0.dp)
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .offset(y = 5.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                color =
                                if (currentRoute == screen.route)
                                    ColorPalette.SelectedIconColor
                                else
                                    ColorPalette.BackgroundIconColor
                            )
                            .padding(10.dp)
                    ){
                        Icon(screen.icon, contentDescription = screen.title)
                    }
                     },
                label = {
                    Text(screen.title,
                        style = TextStyle(fontSize = 16.sp,
                            fontWeight = FontWeight.Medium)
                    )},
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
                    unselectedTextColor = ColorPalette.IconColor,
                    selectedTextColor = ColorPalette.IconColor,
                    selectedIconColor = ColorPalette.BackgroundIconColor,
                    unselectedIconColor = ColorPalette.IconColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}