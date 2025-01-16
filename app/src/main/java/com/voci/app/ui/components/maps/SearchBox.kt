package com.voci.app.ui.components.maps

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.voci.app.data.util.Resource
import com.voci.app.di.LocalServiceLocator
import com.voci.app.ui.components.core.CustomFAB
import com.voci.app.ui.components.core.LocationHandler
import com.voci.app.ui.components.homeless.AddLocationSearchbar

@Composable
fun SearchBox(
    onConfirmLocation: (String) -> Unit,
){
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    val mapboxViewmodel = serviceLocator.obtainMapboxViewModel()
    val locationCoordinates by mapboxViewmodel.locationCoordinates.collectAsState()
    val locationAddress by mapboxViewmodel.locationAddress.collectAsState()
    // Initialize camera location and points
    var points by remember { mutableStateOf<List<Point>>(emptyList()) }
    var address by remember { mutableStateOf("") }
    var cameraOptions by remember { mutableStateOf<CameraOptions?>(null) }
    // Current Location
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationHandler = remember {
        LocationHandler(context, fusedLocationClient)
    }
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    var showLocationSelectionDialog by remember { mutableStateOf(false) }

    // Fetch location coordinates
    when (locationCoordinates) {
        is Resource.Loading -> {  }
        is Resource.Success -> {
            val coordinates = locationCoordinates.data!!
            val point = Point.fromLngLat(coordinates.second, coordinates.first)
            points = listOf(Point.fromLngLat(coordinates.second, coordinates.first))
            cameraOptions = CameraOptions.Builder()
                .center(Point.fromLngLat(coordinates.second, coordinates.first))
                .pitch(45.0)
                .zoom(15.0)
                .bearing(-17.6)
                .build()
            mapboxViewmodel.reverseGeocoding(point.latitude(), point.longitude())
        }
        is Resource.Error -> {
            Log.e("SearchBox", "Error loading location coordinates: ${locationCoordinates.message}")
        }
    }

    LaunchedEffect(locationAddress) {
        if (locationAddress is Resource.Success) {
            address = locationAddress.data ?: ""
            Log.d("SearchBox", "Address: $address")
        }
    }

    LaunchedEffect(Unit) {
        locationHandler.getCurrentLocation(
            callback = { location ->
                currentLocation = location
            }
        )
        mapboxViewmodel.reverseGeocoding(
            currentLocation?.first ?: 0.0,
            currentLocation?.second ?: 0.0
        )
    }
    //----- Region: View Composition -----
    Box(modifier = Modifier.fillMaxSize()) {
        // MapView with points and camera options
        MultiPointMap(
            points = points,
            cameraOptions = cameraOptions ?:
                CameraOptions
                    .Builder()
                    .center(Point.fromLngLat(9.19, 45.4642))
                    .pitch(45.0)
                    .zoom(10.5)
                    .bearing(-17.6)
                    .build()
        )
        // Searchbar with suggestions
        AddLocationSearchbar(
            modifier = Modifier.align(Alignment.TopCenter),
            onClick = {
                address = it
                mapboxViewmodel.forwardGeocoding(it, proximity = "${currentLocation?.second},${currentLocation?.first}")
                Log.d("SearchBox-AddLocationSearchBar", it)
            }
        )
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.align(Alignment.BottomEnd)
        ){
            // Button to save current location - TOP
            CustomFAB(
                icon = Icons.Default.LocationOn,
                onClick = {
                    Log.d("SearchBox", "button: ${locationAddress.data.toString()}")
                    address = locationAddress.data ?: ""
                    mapboxViewmodel.forwardGeocoding(address, proximity = "${currentLocation?.second},${currentLocation?.first}")
                },
            )
            // Button to save the selected location - BOTTOM
            CustomFAB(
                text = "Conferma",
                icon = Icons.Default.Check,
                onClick = {
                    showLocationSelectionDialog = true
                },
            )
        }
    }

    if (showLocationSelectionDialog) {
        LocationSelectionDialog(
            location = address,
            onConfirmLocation = { onConfirmLocation(it) },
            onDismiss = { showLocationSelectionDialog = false }
        )
    }
}