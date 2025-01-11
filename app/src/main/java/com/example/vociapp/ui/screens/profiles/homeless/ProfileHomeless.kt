package com.example.vociapp.ui.screens.profiles.homeless

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.core.ProfileRequestList
import com.example.vociapp.ui.components.core.hapticFeedback
import com.example.vociapp.ui.components.homeless.AddAddressDialog
import com.example.vociapp.ui.components.homeless.LocationHandler
import com.example.vociapp.ui.components.maps.StaticMap
import com.example.vociapp.ui.components.updates.UpdateLastItem
import com.example.vociapp.ui.components.updates.UpdateListDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable
fun ProfileHomelessScreen(
    navController: NavHostController,
    homelessID: String
) {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()

    val homelessState by homelessViewModel.specificHomeless.collectAsState()
    val locationState by homelessViewModel.locationCoordinates.collectAsState()

    val requestViewModel = serviceLocator.obtainRequestViewModel()
    val requests by requestViewModel.requestsByHomelessId.collectAsState()

    val updateViewModel = serviceLocator.obtainUpdatesViewModel()
    val updates by updateViewModel.updatesByHomelessId.collectAsState()

    var showUpdateListDialog by remember { mutableStateOf(false) }
    var showAddAddressDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationHandler = remember {
        LocationHandler(context, fusedLocationClient, homelessViewModel)
    }

    LaunchedEffect(homelessID) {
        homelessID.let {
            homelessViewModel.getHomelessDetailsById(it)
            requestViewModel.getRequestsByHomelessId(it)
            updateViewModel.getUpdatesByHomelessId(it)
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
                    //Success implies homeless is not null
                    val homeless = homelessState.data!!

                    //Info principali
                    InfoList(homeless)
                    //Mappa
                    LocationFrame(locationState = locationState)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    //Richieste attive
                    Text(text = "Richieste Attive")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        Column {
                            ProfileRequestList(
                                requests = requests,
                                navController = navController,
                                homelessId = homelessID,
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    //Aggiornamenti
                    Text(text = "Aggiornamenti")
                    Box(
                        modifier = Modifier
                            .clickable { showUpdateListDialog = true },
                    ) {
                        if (showUpdateListDialog) {
                            UpdateListDialog(
                                onDismiss = { showUpdateListDialog = false },
                                updates = updates,
                            )
                        }
                        if (updates.data?.isNotEmpty() == true){
                            UpdateLastItem(
                                updates = updates,
                                navController = navController,
                                homelessViewModel = homelessViewModel
                            )
                        } else {
                            Text(
                                text = "Non ci sono aggiornamenti",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                is Resource.Error -> {
                    // Display the error message
                    val errorMessage = (homelessState as Resource.Error).message
                    Text(text = errorMessage ?: "Errore sconosciuto")
                }
            }
        }
        //Aggiorna posizione attuale
        FloatingActionButton(
            onClick = { showAddAddressDialog = true },
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
                    Icons.Filled.LocationOn,
                    contentDescription = "Save Location",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        if (showAddAddressDialog) {
            AddAddressDialog(
                onDismiss = {
                    showAddAddressDialog = false
                    homelessViewModel.getHomelessDetailsById(homelessID)
                },
                homelessId = homelessID,
                homelessViewModel = homelessViewModel,
                locationHandler = locationHandler
            )
        }
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${homeless.age}  -",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${homeless.gender}  -",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = homeless.nationality,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = "Descrizione: ${homeless.description}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Indirizzo: ${homeless.location}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

// MAPPA Ultima posizione del senzatetto
@Composable
fun LocationFrame(locationState: Resource<Pair<Double, Double>>) {
    when (locationState) {
        is Resource.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
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
}
