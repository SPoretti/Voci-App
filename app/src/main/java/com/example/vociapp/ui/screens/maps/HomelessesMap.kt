package com.example.vociapp.ui.screens.maps

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.core.LocationHandler
import com.example.vociapp.ui.components.maps.MultiPointMap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions

@Composable
fun HomelessesMap(
    homelessId: String
) {
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    // Homeless Data
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    var homeless by remember { mutableStateOf<Homeless?>(null) }
    // Mapbox Data
    val mapboxViewModel = serviceLocator.obtainMapboxViewModel()
    val locationCoordinates = mapboxViewModel.locationCoordinates.collectAsState().value
    val locations by homelessViewModel.locations.collectAsState()
    var points by remember { mutableStateOf<List<Point>>(emptyList()) }
    // Location Data
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationHandler = remember {
        LocationHandler(context, fusedLocationClient)
    }
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    Log.d("HomelessesMap", "1 LocationCoordinates: ${locationCoordinates.data.toString()}")
    // Initial Setup
    LaunchedEffect(Unit) {
        locationHandler.getCurrentLocation(
            callback = { location ->
                currentLocation = location
            }
        )
        homelessViewModel.getHomelessById(homelessId).let {
            Log.d("HomelessesMap", "1.1 LocationCoordinates: ${locationCoordinates.data.toString()}")
            homeless = it
            if(homeless != null){
                Log.d("HomelessesMap", "1.2 LocationCoordinates: ${locationCoordinates.data.toString()}")
                mapboxViewModel.forwardGeocoding(
                    homeless!!.location,
                    proximity = "${currentLocation?.second},${currentLocation?.first}"
                )
                Log.d("HomelessesMap", "1.3 LocationCoordinates: ${locationCoordinates.data.toString()}")
            }
        }
        homelessViewModel.getAllCoordinates()
    }
    // Update the points based on the locations
    when (locations) {
        is Resource.Success -> {
            points = locations.data?.map { coordinate ->
                Point.fromLngLat(coordinate.second, coordinate.first)
            } ?: emptyList()
            Log.d("HomelessesMap", "Points: $points")
        }
        is Resource.Error -> {
            Log.e("HomelessesMap", "Error loading locations: ${locations.message}")
            // Handle error state (e.g., show a snackbar)
        }
        is Resource.Loading -> {
            Log.d("HomelessesMap", "Loading locations")
            CircularProgressIndicator()
        }
    }
    // Map View
    MultiPointMap(
        points = points,
        cameraOptions = if(homeless == null){
            CameraOptions.Builder()
                .center(Point.fromLngLat(9.19, 45.4642))
                .zoom(10.0)
                .build()
        } else {
            CameraOptions.Builder()
                .center(Point.fromLngLat(locationCoordinates.data?.second ?: 9.19,
                    locationCoordinates.data?.first ?: 45.4642
                ))
                .zoom(15.0)
                .build()
        },
        homeless = homeless
    )
}