package com.example.vociapp.ui.components.maps

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.core.CustomFAB
import com.example.vociapp.ui.components.homeless.AddLocationSearchbar
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions

@Composable
fun SearchBox(
    onConfirmLocation: () -> Unit
){
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val locationState by homelessViewModel.locationCoordinates.collectAsState()
    // Initialize camera location and points
    var cameraLocation by remember { mutableStateOf<Point?>(null) }
    var points by remember { mutableStateOf<List<Point>>(emptyList()) }
    var address by remember { mutableStateOf("") }
    var cameraOptions by remember { mutableStateOf<CameraOptions?>(null) }
    // Fetch location coordinates
    when (locationState) {
        is Resource.Loading -> {
            CircularProgressIndicator()
        }
        is Resource.Success -> {
            val coordinates = locationState.data
            if (coordinates != null) {
                cameraLocation = Point.fromLngLat(coordinates.second, coordinates.first)
                points = listOf(Point.fromLngLat(coordinates.second, coordinates.first))
                cameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(coordinates.second, coordinates.first))
                    .pitch(45.0)
                    .zoom(15.0)
                    .bearing(-17.6)
                    .build()
            }
        }
        is Resource.Error -> {
            // Handle error state (e.g., show an error message)
        }
    }
    //----- Region: View Composition -----
    Box(modifier = Modifier.fillMaxSize()) {
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
        AddLocationSearchbar(
            modifier = Modifier.align(Alignment.TopCenter),
            onClick = {
                address = it
                homelessViewModel.geocodeAddress(it)
                Log.d("ApiTesting", it)
            }
        )
        // Button to save current location
        CustomFAB(
            text = "Confirm Location",
            icon = Icons.Default.LocationOn,
            onClick = onConfirmLocation,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}