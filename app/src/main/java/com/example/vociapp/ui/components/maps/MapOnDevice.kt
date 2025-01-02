package com.example.vociapp.ui.components.maps

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vociapp.ui.components.utils.hapticFeedback
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MapOnDevice(
    onAddNewLocation: (Point) -> Unit
){
    val mapViewportState = rememberMapViewportState()
    var location by remember { mutableStateOf<Point?>(null) }
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        MapboxMap(
            Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
        ) {
            MapEffect(Unit) { mapView ->
                val locationComponentPlugin = mapView.location
                locationComponentPlugin.updateSettings {
                    enabled = true
                }

                // Get location updates
                locationComponentPlugin.addOnIndicatorPositionChangedListener { point ->
                    location = point
                    Log.d("MyMapApp:Location", point.toString())
                }

                mapView.location.updateSettings {
                    locationPuck = createDefault2DPuck(withBearing = true)
                    enabled = true
                    puckBearing = PuckBearing.COURSE
                    puckBearingEnabled = true
                }
                mapViewportState.transitionToFollowPuckState()
            }
        }

        FloatingActionButton(
            onClick = {
                // Add a marker (if location is not null)
                location?.let {
                    onAddNewLocation(it)
                }
                Log.d("MyMapApp:Location", location?.coordinates().toString())
            },
            elevation = FloatingActionButtonDefaults.elevation(50.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .hapticFeedback(),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(Icons.Filled.LocationOn, contentDescription = "Save Location", tint = MaterialTheme.colorScheme.onBackground)
                Text("Save Location", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }

}