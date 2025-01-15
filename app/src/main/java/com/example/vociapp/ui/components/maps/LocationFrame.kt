package com.example.vociapp.ui.components.maps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator

@Composable
fun LocationFrame(locationState: Resource<Pair<Double, Double>>) {
    val serviceLocator = LocalServiceLocator.current
    val networkManager = serviceLocator.obtainNetworkManager()
    val isOnline = networkManager.isNetworkConnected()
    if(isOnline){
        when (locationState) {
            is Resource.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is Resource.Success -> {
                val coordinates =
                    locationState.data
                if (coordinates != null) {
                    StaticMap(
                        latitude = coordinates.first,
                        longitude = coordinates.second
                    )
                } else {
                    Text(text = "Coordinate non disponibili")
                }
            }

            is Resource.Error -> {
                val errorMessage = locationState.message
                Text(text = "Errore coordinate: ${errorMessage ?: "Errore sconosciuto"}")
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceDim)
                .height(200.dp),
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Nessuna connessione internet")
                Image(
                    imageVector = Icons.Default.ImageNotSupported,
                    contentDescription = "No internet connection"
                )
            }
        }
    }
}