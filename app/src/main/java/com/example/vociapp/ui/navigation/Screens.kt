package com.example.vociapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screens("home", "Home" , Icons.Filled.Home)
    object SignIn : Screens("signIn", "Sign In", Icons.Filled.Person)
    object SignUp : Screens("signUp", "Sign Up", Icons.Filled.Person)
    object Requests : Screens("requests", "Richieste", Icons.Filled.Inbox)
    object RequestsHistory : Screens("requestsHistory", "Requests History", Icons.Filled.ShoppingCart)
    object UserProfile : Screens("userProfile", "Profilo", Icons.Filled.PersonOutline)
    object UpdateUserProfile : Screens("updateUserProfile", "Update User Profile", Icons.Filled.Person)
}