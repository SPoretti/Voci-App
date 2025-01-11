package com.example.vociapp.ui.screens

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.vociapp.ui.components.maps.SearchBox

@Composable
fun ApiTesting(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    // Thias: 9.033408, 45.527823
    // Milano: 9.19, 45.4642
    SearchBox(
        onConfirmLocation = {
            Log.d("ApiTestingScreen", it)
        }
    )
}