package com.example.vociapp.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.vociapp.ui.components.maps.MultiPointMap
import com.mapbox.geojson.Point
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun ApiTesting(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    // Thias: 9.033408, 45.527823
    // Milano: 9.19, 45.4642

//    MapOnDevice(
//        onAddNewLocation = {
//            // Add a new location to the database
//            Log.d("ApiTesting:Location", it.toString())
//        }
//    )

//    MapWithMarker(
//        location = Point.fromLngLat(9.033408, 45.527823)
//    )

    val centerLat = 45.4642
    val centerLon = 9.19
    val radiusInMeters = 5000.0 // 5km radius

    val points = (1..10).map {
        val randomAngle = Random.nextDouble() * 2 * Math.PI
        val randomRadius = Random.nextDouble() * radiusInMeters

        val x = centerLon + (randomRadius * cos(randomAngle) / (111320 * cos(
            Math.toRadians(
                centerLat
            )
        )))
        val y = centerLat + (randomRadius * sin(randomAngle) / 111320)

        Point.fromLngLat(x, y)
    }

    MultiPointMap(
        points = points,
        cameraLocation = Point.fromLngLat(9.19, 45.4642)
    )

}