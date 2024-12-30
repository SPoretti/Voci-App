package com.example.vociapp.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.vociapp.ui.components.maps.MapOnDevice

@Composable
fun ApiTesting(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {

    MapOnDevice(
        onAddNewLocation = {  }
    )

    // Thias: 9.033408, 45.527823
    // Milano: 9.19, 45.4642

//    MapWithMarker(
//        location = Point.fromLngLat(9.033408, 45.527823)
//    )

//    MapboxSearchComponent()

//    ShowSearchHistory()

}