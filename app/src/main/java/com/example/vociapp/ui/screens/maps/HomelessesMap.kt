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
import com.example.vociapp.ui.components.homeless.LocationHandler
import com.example.vociapp.ui.components.maps.MultiPointMap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions

@Composable
fun HomelessesMap( homelessId: String) {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    var homeless by remember { mutableStateOf<Homeless?>(null) }
    val locationCoordinates = homelessViewModel.locationCoordinates.collectAsState().value
    val locations by homelessViewModel.locations.collectAsState()
    var points by remember { mutableStateOf<List<Point>>(emptyList()) }
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationHandler = remember {
        LocationHandler(context, fusedLocationClient)
    }
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    Log.d("HomelessesMap", "1 - Locations: ${locations.data.toString()}")

    LaunchedEffect(Unit) {
        locationHandler.getCurrentLocation(
            callback = { location ->
                currentLocation = location
            }
        )
        homelessViewModel.getHomelessById(homelessId).let {
            homeless = it
            if(homeless != null){
                homelessViewModel.mapboxForwardGeocoding(
                    homeless!!.location,
                    proximity = "${currentLocation?.second},${currentLocation?.first}"
                )
            }
        }
        homelessViewModel.getAllCoordinates()
    }

    Log.d("HomelessesMap", "2 - Locations: ${locations.data.toString()}")

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
        }
    )
}