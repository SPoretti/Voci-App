package com.example.vociapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screens("home", title = "Home" , icon = Icons.Filled.Home)
    object UserProfile : Screens("userProfile", "Profile", Icons.Filled.Person)
    object UpdateUserProfile : Screens("updateUserProfile", "Update User Profile", Icons.Filled.Person)
    object SignIn : Screens("signIn", "Sign In", Icons.Filled.Person)
    object SignUp : Screens("signUp", "Sign Up", Icons.Filled.Person)
}