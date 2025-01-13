package com.example.vociapp.ui.components.homeless

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationHandler @Inject constructor(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {

    suspend fun getCurrentLocation(callback: (Pair<Double, Double>?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("LocationHandler", "No permissions")
            callback(null)
            return
        }
        try {
            val location: Location? = fusedLocationClient.lastLocation.await()
            if (location != null) {
                Log.d("LocationHandler", "Location found: ${location.latitude}, ${location.longitude}")
                callback(Pair(location.latitude, location.longitude))
            } else {
                Log.d("LocationHandler", "Last location is null, trying to get current location")
                val currentLocation = fusedLocationClient.getCurrentLocation(
                    100,
                    null
                ).await()
                if (currentLocation != null) {
                    Log.d("LocationHandler", "Current location found: ${currentLocation.latitude}, ${currentLocation.longitude}")
                    callback(Pair(currentLocation.latitude, currentLocation.longitude))
                } else {
                    Log.d("LocationHandler", "Current location is null")
                    callback(null)
                }
            }
        } catch (e: Exception) {
            Log.e("LocationHandler", "Error getting location: ${e.message}")
            callback(null)
        }
    }
}