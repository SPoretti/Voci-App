package com.example.vociapp.ui.components.homeless

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewModelScope
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

@Composable
fun AddAddressDialog(
    onDismiss: () -> Unit,
    homelessId: String,
    homelessViewModel: HomelessViewModel
) {
    val context = LocalContext.current
    var addressText by remember { mutableStateOf("Loading address...") }
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    val hasLocationPermission = remember {
        mutableStateOf(
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    fun getCurrentLocation() {
        if (hasLocationPermission.value) {
            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            homelessViewModel.viewModelScope.launch {
                                homelessViewModel.reverseGeocodeLocation(location.latitude, location.longitude)
                            }
                        } else {
                            addressText = "Location not found."
                        }
                    }
                    .addOnFailureListener {
                        addressText = "Failed to get location."
                    }
            } catch (e: SecurityException) {
                addressText = "Location permission not granted."
            }
        } else {
            addressText = "Location permission not granted."
        }
    }

    LaunchedEffect(key1 = true) {
        getCurrentLocation()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                getCurrentLocation()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = homelessViewModel.locationAddress.value) {
        homelessViewModel.locationAddress.value.let { resource ->
            when (resource) {
                is com.example.vociapp.data.util.Resource.Success -> {
                    addressText = resource.data?: "Loading address..."
                }

                is com.example.vociapp.data.util.Resource.Error -> {
                    addressText = "Errore: ${resource.message}"
                }

                else -> {}
            }
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        shape = RoundedCornerShape(0.dp),
        title = { Text("Sei sicuro?") },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "Vuoi aggiornare la posizione attuale del senzatetto?",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text("Indirizzo: $addressText")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    homelessViewModel.updateHomelessLocation(homelessId, addressText)
                    onDismiss()
                }
            ) {
                Text("Aggiorna")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                Text("Annulla")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground,
    )
}