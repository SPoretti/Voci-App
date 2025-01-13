package com.example.vociapp.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.vociapp.ui.components.core.SwipeDirection
import com.example.vociapp.ui.components.core.SwipeableScreen

@Composable
fun ApiTesting(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    // Thias: 9.033408, 45.527823
    // Milano: 9.19, 45.4642

    SwipeableScreen(
        navController = navController,
        targetRoute = "home",
        direction = SwipeDirection.LEFT
    ) {
        Text("Swipe to home")
    }

}