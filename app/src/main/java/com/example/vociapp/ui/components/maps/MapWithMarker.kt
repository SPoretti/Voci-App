package com.example.vociapp.ui.components.maps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.vociapp.R
import com.mapbox.geojson.Point
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.animation.MapAnimationOptions

@Composable
fun MapWithMarker(
    location: Point
) {
    val mapViewportState = rememberMapViewportState()

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
    ) {
        val marker = rememberIconImage(resourceId = R.drawable.marker_icon)

        PointAnnotation(point = location) {
            iconImage = marker
        }

        MapEffect(Unit) { mapView ->
            // Optionally, update camera to focus on the location
            mapViewportState.easeTo (
                cameraOptions {
                    center(location)
                    pitch(45.0)
                    zoom(15.5)
                    bearing(-17.6)
                    zoom(14.5)
                },
                animationOptions = MapAnimationOptions.mapAnimationOptions {
                    duration(1000)
                }
            )
        }
    }
}
