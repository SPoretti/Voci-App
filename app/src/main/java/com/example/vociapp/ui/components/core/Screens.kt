package com.example.vociapp.ui.components.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screens("home", "Home" , Icons.Filled.Home)
    object Requests : Screens("requests", "Richieste", Icons.Filled.Inbox)
}