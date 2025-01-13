package com.example.vociapp.ui.screens.maps

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.maps.MultiPointMap
import com.mapbox.geojson.Point

@Composable
fun HomelessesMap() {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    homelessViewModel.getLocations()

    val locationsResource by homelessViewModel.locations.collectAsState()

    when (locationsResource) {
        is Resource.Success -> {
            val points = locationsResource.data?.map { (lat, lon) ->
                Point.fromLngLat(lon, lat)
            } ?: emptyList()

            MultiPointMap(
                points = points,
            )
        }

        is Resource.Error -> {
            Log.d("HomelessesMap", "Error: ${locationsResource.message}")
            // Display an error message to the user
        }

        is Resource.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Log.d("HomelessesMap", "Loading")
            // Display a loading indicator to the user
        }
    }
}