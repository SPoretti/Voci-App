package com.example.vociapp.ui.components.maps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.vociapp.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions

@Composable
fun MultiPointMap(
    points: List<Point>,        // List of points to display
    cameraOptions: CameraOptions = CameraOptions.Builder().center(Point.fromLngLat(9.19, 45.4642)).zoom(10.0).build(),       // Initial camera location
) {
    //----- Region: Data Initialization -----
    val mapViewportState = rememberMapViewportState()

    //----- Region: View Composition -----
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
        style = { MapStyle(style = Style.TRAFFIC_NIGHT) }
    ) {
        // Load the icon image
        val marker = rememberIconImage(resourceId = R.drawable.marker_icon)
        // Add points to the map
        points.forEach { point ->
            PointAnnotation(point = point) {
                iconImage = marker
            }
        }
        // Set the initial camera position with the location
//        MapEffect(Unit) { mapView ->
//            mapViewportState.easeTo (
//                cameraOptions {
//                    center(Point.fromLngLat(9.19, 45.4642))
//                    pitch(45.0)
//                    zoom(10.5)
//                    bearing(-17.6)
//                },
//                animationOptions = MapAnimationOptions.mapAnimationOptions {
//                    duration(1000)
//                }
//            )
//        }
        LaunchedEffect(cameraOptions) {
            mapViewportState.easeTo (
                cameraOptions {
                    center(cameraOptions.center)
                    pitch(cameraOptions.pitch)
                    zoom(cameraOptions.zoom)
                    bearing(cameraOptions.bearing)
                },
                animationOptions = MapAnimationOptions.mapAnimationOptions {
                    duration(1000)
                }
            )
        }
    }
}