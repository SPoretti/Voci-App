package com.example.vociapp.ui.screens.profiles.homeless

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Transgender
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.UpdateStatus
import com.example.vociapp.data.util.Gender
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.core.CustomChip
import com.example.vociapp.ui.components.core.CustomFAB
import com.example.vociapp.ui.components.core.ProfileRequestList
import com.example.vociapp.ui.components.core.StatusLED
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
    navController: NavHostController,   // Pass the navController
    homelessID: String                  // Pass the homelessID
) {
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    // Homeless
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val specificHomeless by homelessViewModel.specificHomeless.collectAsState()
    val locationState by homelessViewModel.locationCoordinates.collectAsState()
    // Requests
    val requestViewModel = serviceLocator.obtainRequestViewModel()
    val requests by requestViewModel.requestsByHomelessId.collectAsState()
    // Updates
    val updateViewModel = serviceLocator.obtainUpdatesViewModel()
    val updates by updateViewModel.updatesByHomelessId.collectAsState()
    // Dialogs
    var showUpdateListDialog by remember { mutableStateOf(false) }
    var showAddAddressDialog by remember { mutableStateOf(false) }
    // Location
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationHandler = remember {
        LocationHandler(context, fusedLocationClient, homelessViewModel)
    }
    // Init
    LaunchedEffect(homelessID) {
        homelessID.let {
            homelessViewModel.getHomelessDetailsById(it)
            requestViewModel.getRequestsByHomelessId(it)
            updateViewModel.getUpdatesByHomelessId(it)
        }
    }
    //----- Region: View Composition -----
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
            when (specificHomeless) {
                is Resource.Loading -> {
                    // Show a loading indicator
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    //Success implies homeless is not null
                    val homeless = specificHomeless.data!!

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
                                onConfirm = {
                                    navController.navigate("UpdatesAddScreen/${homeless.id}")
                                }
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
                    val errorMessage = (specificHomeless as Resource.Error).message
                    Text(text = errorMessage ?: "Errore sconosciuto")
                }
            }
        }
        //Aggiorna posizione attuale
        CustomFAB(
            onClick = { showAddAddressDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd),
            icon = Icons.Filled.LocationOn,
            text = "Posizione Attuale"
        )
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
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = homeless.name,
                style = MaterialTheme.typography.headlineLarge
            )
            StatusLED(
                // Map the status to the color
                color = when (homeless.status){
                    UpdateStatus.GREEN -> Color.Green
                    UpdateStatus.YELLOW -> Color.Yellow
                    UpdateStatus.RED -> Color.Red
                    UpdateStatus.GRAY -> Color.Gray
                },
                // If color is gray don't pulsate
                isPulsating = when(homeless.status){
                    UpdateStatus.GREEN -> true
                    UpdateStatus.YELLOW -> true
                    UpdateStatus.RED -> true
                    UpdateStatus.GRAY -> false
                },
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            CustomChip(
                text = if(homeless.age == "") "?" else ("${homeless.age} anni"),
                onClick = {},
                imageVector = Icons.Default.CalendarMonth
            )
            Spacer(modifier = Modifier.width(4.dp))
            when(homeless.gender){
                Gender.Female -> {
                    CustomChip(
                        text = "${homeless.gender}",
                        onClick = {},
                        imageVector = Icons.Default.Female,
                    )
                }
                Gender.Male -> {
                    CustomChip(
                        text = "${homeless.gender}",
                        onClick = {},
                        imageVector = Icons.Default.Male
                    )
                }
                Gender.Unspecified -> {
                    CustomChip(
                        text = "${homeless.gender}",
                        onClick = {},
                        imageVector = Icons.Default.Transgender
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            CustomChip(
                text = if(homeless.nationality == "") "?" else (homeless.nationality),
                onClick = {},
                imageVector = Icons.Default.Public
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = homeless.description,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = homeless.location,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
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
