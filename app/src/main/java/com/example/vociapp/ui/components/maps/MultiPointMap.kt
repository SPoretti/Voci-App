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
fun MultiPointMap(
    points: List<Point>,
    cameraLocation: Point
) {
    val mapViewportState =
        rememberMapViewportState()

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
    ) {
        val marker = rememberIconImage(resourceId = R.drawable.marker_icon)

        points.forEach { point ->
            PointAnnotation(point = point) {
                iconImage = marker
            }
        }

        MapEffect(Unit) { mapView ->
            mapViewportState.easeTo (
                cameraOptions {
                    center(cameraLocation)
                    pitch(45.0)
                    zoom(10.5)
                    bearing(-17.6)
                },
                animationOptions = MapAnimationOptions.mapAnimationOptions {
                    duration(1000)
                }
            )
        }
    }
}