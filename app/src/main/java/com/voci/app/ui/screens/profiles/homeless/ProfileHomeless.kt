package com.voci.app.ui.screens.profiles.homeless

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.voci.app.data.util.Resource
import com.voci.app.di.LocalServiceLocator
import com.voci.app.ui.components.core.CustomFAB
import com.voci.app.ui.components.core.ProfileRequestList
import com.voci.app.ui.components.homeless.CustomHomelessDialog
import com.voci.app.ui.components.homeless.DescriptionDialog
import com.voci.app.ui.components.homeless.HomelessDeletionDialog
import com.voci.app.ui.components.homeless.HomelessInfo
import com.voci.app.ui.components.maps.LocationFrame
import com.voci.app.ui.components.updates.UpdateLastItem
import com.voci.app.ui.components.updates.UpdateListDialog

@Composable
fun ProfileHomelessScreen(
    navController: NavHostController,   // Pass the navController
    homelessID: String                  // Pass the homelessID
) {
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    // Homeless
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val mapboxViewModel = serviceLocator.obtainMapboxViewModel()
    val specificHomeless by homelessViewModel.specificHomeless.collectAsState()
    val locationState by mapboxViewModel.locationCoordinates.collectAsState()

    // Requests
    val requestViewModel = serviceLocator.obtainRequestViewModel()
    val requests by requestViewModel.requestsByHomelessId.collectAsState()
    // Updates
    val updateViewModel = serviceLocator.obtainUpdatesViewModel()
    val updates by updateViewModel.updatesByHomelessId.collectAsState()
    // Dialogs
    var showUpdateListDialog by remember { mutableStateOf(false) }
    var showModifyHomelessDialog by remember { mutableStateOf(false) }
    var showDescriptionDialog by remember { mutableStateOf(false) }
    var showDeletionDialog by remember { mutableStateOf(false) }
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
        modifier = Modifier.fillMaxSize()
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

                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            //Info principali
                            HomelessInfo(homeless, openDescription = {
                                showDescriptionDialog = true
                            })
                            if (showDescriptionDialog) {
                                DescriptionDialog(
                                    description = homeless.description,
                                    onDismiss = { showDescriptionDialog = false }
                                )
                            }
                        }
                        item {
                            //Mappa
                            LocationFrame(locationState = locationState)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {
                            //Richieste attive
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Richieste Attive",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
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
                        }
                        item {
                            //Aggiornamenti
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Aggiornamenti",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
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
                                if (updates.data?.isNotEmpty() == true) {
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
                        }
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }

                is Resource.Error -> {
                    // Display the error message
                    val errorMessage = (specificHomeless as Resource.Error).message
                    Text(text = errorMessage ?: "Errore sconosciuto")
                }
            }
        }
        // Elimina senzatetto - LEFT
        CustomFAB(
            onClick = {
                showDeletionDialog = true
                homelessViewModel.deleteHomeless(specificHomeless.data!!)
            },
            modifier = Modifier.align(Alignment.BottomStart),
            icon = Icons.Filled.Delete,
            text = "Elimina"
        )
        // Aggiorna senzatetto - RIGHT
        CustomFAB(
            onClick = { showModifyHomelessDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd),
            icon = Icons.Filled.Edit,
            text = "Modifica"
        )
        if (showModifyHomelessDialog) {
            CustomHomelessDialog(
                onDismiss = {
                    showModifyHomelessDialog = false
                },
                onConfirm = {
                    homelessViewModel.updateHomeless(it)
                    showModifyHomelessDialog = false
                },
                homeless = specificHomeless.data!!,
                actionText = "Modifica"
            )
        }

        if (showDeletionDialog) {
            HomelessDeletionDialog(
                homelessViewModel = homelessViewModel,
                onDismiss = {
                    showDeletionDialog = false
                    navController.navigate("home")
                }
            )
        }
    }
}
