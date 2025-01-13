package com.example.vociapp.ui.screens.profiles.homeless

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import com.example.vociapp.ui.components.homeless.CustomHomelessDialog
import com.example.vociapp.ui.components.homeless.AddAddressDialog
import com.example.vociapp.ui.components.homeless.HomelessInfo
import com.example.vociapp.ui.components.homeless.LocationHandler
import com.example.vociapp.ui.components.maps.LocationFrame
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
    var showModifyHomelessDialog by remember { mutableStateOf(false) }
    // Location
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationHandler = remember {
        LocationHandler(context, fusedLocationClient)
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
                    HomelessInfo(homeless)
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
        //Aggiorna senzatetto
        CustomFAB(
            onClick = { showModifyHomelessDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd),
            icon = Icons.Filled.Edit,
            text = "Modifica"
        )
        if (showModifyHomelessDialog) {
            CustomHomelessDialog(
                onDismiss = { showModifyHomelessDialog = false },
                onConfirm = { homelessViewModel.updateHomeless(it) },
                homeless = specificHomeless.data!!,
                actionText = "Modifica"
            )
        }
    }
}
