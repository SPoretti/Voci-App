package com.voci.app.ui.components.maps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.voci.app.R
import com.voci.app.data.local.database.Homeless
import com.voci.app.di.LocalServiceLocator
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions

@Composable
fun MultiPointMap(
    points: List<Point>,        // List of points to display
    homeless: Homeless? = null, // Homeless data to display
    cameraOptions: CameraOptions = CameraOptions.Builder().center(Point.fromLngLat(9.19, 45.4642)).zoom(10.0).build(),       // Initial camera location
) {
    //----- Region: Data Initialization -----
    val mapViewportState = rememberMapViewportState()
    val isDarkTheme = isSystemInDarkTheme()

    val networkManager = LocalServiceLocator.current.obtainNetworkManager()
    val isOnline = networkManager.isNetworkConnected()

    //----- Region: View Composition -----
    if (isOnline) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = mapViewportState,
                style = {
                    if (isDarkTheme) {
                        MapStyle(style = Style.DARK)
                    } else {
                        MapStyle(style = Style.STANDARD)
                    }
                },
                compass = {
                    Spacer(modifier = Modifier.height(16.dp))
                },
                scaleBar = {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            ) {
                // Load the icon image
                val marker = rememberIconImage(resourceId = R.drawable.marker_icon)
                // Add points to the map
                points.forEach { point ->
                    PointAnnotation(point = point) {
                        iconImage = marker
                    }
                }
                // Animate the camera to the CameraOption value
                LaunchedEffect(cameraOptions) {
                    mapViewportState.easeTo(
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
            if (homeless != null) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surface)
                        .align(Alignment.TopCenter)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "Posizione di ${homeless.name}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = homeless.location,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.onBackground.copy(
                                    alpha = 0.7f
                                )
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    } else {
        // Placeholder to signify no internet connection
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceDim)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Nessuna connessione internet")
                Image(
                    imageVector = Icons.Default.WifiOff,
                    contentDescription = "No internet connection"
                )
            }
        }
    }
}