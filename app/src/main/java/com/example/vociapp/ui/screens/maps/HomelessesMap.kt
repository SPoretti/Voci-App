package com.example.vociapp.ui.screens.maps

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.maps.MultiPointMap
import com.mapbox.geojson.Point

@Composable
fun HomelessesMap(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    homelessId: String,
) {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    homelessViewModel.getLocations()

    val points = mutableListOf<Point>()
    val coordinates = remember { homelessViewModel.locations.value}
    when (coordinates) {
        is Resource.Success -> {
            for (coordinate in coordinates.data!!) {
                val lat = coordinate.first
                val lon = coordinate.second
                val point = Point.fromLngLat(lon, lat)
                points.add(point)
            }
            MultiPointMap(
                points = points
            )
        }

        is Resource.Error -> { Log.d("HomelessesMap", "Error")}
        is Resource.Loading -> { Log.d("HomelessesMap", "Loading")}
    }
}