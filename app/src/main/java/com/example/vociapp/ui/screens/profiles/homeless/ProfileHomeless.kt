package com.example.vociapp.ui.screens.profiles.homeless

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.utils.hapticFeedback

@Composable
fun ProfileHomelessScreen(homelessID: String?) {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()

    val homelessState by homelessViewModel.homelesses.collectAsState()
    val locationState by homelessViewModel.locationCoordinates.collectAsState()

//    val requestViewModel = serviceLocator.obtainRequestViewModel()
//    val requests by requestViewModel.requests.collectAsState()
//    val requestsState by requestViewModel.requestsByHomelessId.collectAsState()
//    var showAddCommentDialog by remember { mutableStateOf(false) }

    LaunchedEffect(homelessID) {
        homelessID?.let {
            homelessViewModel.fetchHomelessDetailsById(it)
//            requestViewModel.getRequestsByHomelessId(it)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (homelessState) {
                is Resource.Loading -> {
                    // Show a loading indicator
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    // Check if data is null
                    val homelessList = (homelessState as Resource.Success<List<Homeless>>).data
                    if (homelessList != null) {
                        val homeless = homelessList.firstOrNull()
                        if (homeless != null) {
                            InfoList(homeless)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            LocationFrame(locationState=locationState)
                            Spacer(modifier = Modifier.height(16.dp))
                            //RequestsList(requestsState = requestsState)
                            Spacer(modifier = Modifier.height(16.dp))
                            LastComment(homeless)
                            Spacer(modifier = Modifier.height(16.dp))
                        } else {
                            Text(text = "Senzatetto non trovato")
                        }
                    } else {
                        Text(text = "Senzatetto non trovato")
                    }
                }

                is Resource.Error -> {
                    // Display the error message
                    val errorMessage = (homelessState as Resource.Error).message
                    Text(text = errorMessage ?: "Errore sconosciuto")
                }
            }
        }
        FloatingActionButton(
            onClick = { /*showAddCommentDialog = true*/ },
            elevation = FloatingActionButtonDefaults.elevation(50.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .hapticFeedback(),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add comment",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
//        if (showAddCommentDialog) {
//            AddCommentDialog(
//                onDismiss = { showAddCommentDialog = false },
//                onAdd = { comment, date, volunteerId ->
//
//                }
//            )
//        }
    }
}

// INFO Principali del senzatetto
@Composable
fun InfoList(homeless: Homeless) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = homeless.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Row{
            Text(
                text = "Età: ${homeless.age}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Sesso: ${homeless.gender}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Nazionalità: ${homeless.nationality}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = "Descrizione: ${homeless.description}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Indirizzo: ${homeless.location}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// RICHIESTE Attive del senzatetto
@Composable
fun RequestsList(requestsState: Resource<List<Request>>) {
    when (requestsState) {
        is Resource.Loading -> {
            CircularProgressIndicator()
        }

        is Resource.Success -> {
            val requests = requestsState.data ?: emptyList()
            if (requests.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(requests) { request ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "ID: ${request.id}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Descrizione: ${request.description}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Stato: ${request.status}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                // Add more details as needed
                            }
                        }
                    }
                }
            } else {
                Text(text = "Nessuna richiesta trovata.")
            }
        }

        is Resource.Error -> {
            if (requestsState.message != "Requests not found") {
                Text(text = "Errore nel caricamento delle richieste: ${requestsState.message}")
            } else {
                Text(text = "Nessuna richiesta attiva trovata.")
            }
        }
    }
}

@Composable
fun LastComment(homeless: Homeless) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

    }
}

// MAPPA Ultima posizione del senzatetto
@Composable
fun LocationFrame(locationState: Resource<Pair<Double, Double>>) {
    when (locationState) {
        is Resource.Loading -> {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        is Resource.Success -> {
            val coordinates =
                (locationState as Resource.Success<Pair<Double, Double>>).data
            if (coordinates != null) {
                MapboxMap(
                    latitude = coordinates.first,
                    longitude = coordinates.second
                )
            } else {
                Text(text = "Coordinate non disponibili")
            }
        }

        is Resource.Error -> {
            val errorMessage = (locationState as Resource.Error).message
            Text(text = "Errore coordinate: ${errorMessage ?: "Errore sconosciuto"}")
        }
    }
}

// MAPBOX
@Composable
fun MapboxMap(latitude: Double, longitude: Double) {
    val context = LocalContext.current
    val token =
        "pk.eyJ1IjoibXNib3JyYSIsImEiOiJjbTUxZzVkaDgxcHAzMmpzZXIycWgyM2hhIn0.kQRnLhjtCyT8l6LRI-B32g"
    val zoom = 14
    val size = "600x300"
    val pin = "pin-s+ff0000($longitude,$latitude)"

    val imageUrl = "https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/" +
            "$pin/" +
            "$longitude,$latitude,$zoom/" +
            "$size" +
            "?access_token=$token"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clickable {
                val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                try {
                    context.startActivity(mapIntent)
                } catch (e: ActivityNotFoundException) {
                    // Fallback per altri navigatori se Google Maps non è installato
                    val genericUri = Uri.parse("geo:$latitude,$longitude")
                    val genericIntent = Intent(Intent.ACTION_VIEW, genericUri)
                    context.startActivity(genericIntent)
                }
            }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Map location",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
