package com.voci.app.ui.components.maps

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.voci.app.ui.components.core.CustomFAB
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MapOnDevice(
    onAddNewLocation: (Point) -> Unit   // Callback function to add a new location
){
    //----- Region: Data Initialization -----
    val mapViewportState = rememberMapViewportState()
    var location by remember { mutableStateOf<Point?>(null) }

    //----- Region: View Composition -----
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        MapboxMap(
            Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
        ) {
            // Launched effect for Maps
            MapEffect(Unit) { mapView ->
                // Enable location component
                val locationComponentPlugin = mapView.location
                locationComponentPlugin.updateSettings {
                    enabled = true
                }
                // Get location updates
                locationComponentPlugin.addOnIndicatorPositionChangedListener { point ->
                    location = point
                    Log.d("MyMapApp:Location", point.toString())
                }
                // Set initial camera position
                mapView.location.updateSettings {
                    locationPuck = createDefault2DPuck(withBearing = true)
                    enabled = true
                    puckBearing = PuckBearing.HEADING
                    puckBearingEnabled = true
                }
                // Set camera to follow puck
                mapViewportState.transitionToFollowPuckState()
            }
        }
        // Button to save current location
        CustomFAB(
            text = "Add Location",
            icon = Icons.Default.LocationOn,
            onClick = {
                onAddNewLocation(location ?: Point.fromLngLat(0.0, 0.0))
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}