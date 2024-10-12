package com.example.vociapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screens("home", title = "Home" , icon = Icons.Filled.Home)
    object UserProfile : Screens("userProfile", "User Profile", Icons.Filled.Person)
}