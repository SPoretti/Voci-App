package com.example.vociapp.ui.components.homeless

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.example.vociapp.data.util.Resource
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationHandler @Inject constructor(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val homelessViewModel: HomelessViewModel
) {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    suspend fun getCurrentLocationAddress(): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Controlla per i permessi di localizzazione
            if (!hasLocationPermission()) {
                return@withContext Result.failure(Exception("Permessi di localizzazione non concessi"))
            }

            // Controlla se i servizi di localizzazione sono attivi
            if (!isLocationEnabled()) {
                return@withContext Result.failure(Exception("Servizi di localizzazione disattivati"))
            }

            val location = getCurrentLocation()

            // ReverseGeocode per ottenere l'indirizzo a partire dalle coordinate
            homelessViewModel.reverseGeocodeLocation(location.latitude, location.longitude)

            when (val addressResource = homelessViewModel.locationAddress.first()) {
                is Resource.Success -> {
                    val address = addressResource.data
                    if (address != null) {
                        Result.success(address)
                    } else {
                        Result.failure(Exception("Indirizzo non trovato"))
                    }
                }

                is Resource.Error -> Result.failure(Exception(addressResource.message))
                is Resource.Loading -> Result.failure(Exception("Caricamento non completato"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private suspend fun getCurrentLocation(): Location = suspendCancellableCoroutine { continuation ->
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        continuation.resume(location)
                    } else {
                        continuation.resumeWithException(Exception("Location not found"))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        } catch (e: SecurityException) {
            continuation.resumeWithException(e)
        }
    }
}