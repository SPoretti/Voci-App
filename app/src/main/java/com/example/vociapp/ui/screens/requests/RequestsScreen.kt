package com.example.vociapp.ui.screens.requests

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.RequestStatus
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.requests.AddRequestDialog
import com.example.vociapp.ui.components.requests.RequestList
import com.example.vociapp.ui.components.requests.SortButtons
import com.example.vociapp.ui.components.utils.hapticFeedback
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.state.SortOption
import kotlinx.coroutines.launch

@Composable
fun RequestsScreen(
    navController: NavHostController,       // Navigation controller for navigation
    snackbarHostState: SnackbarHostState    // Snackbar host state for displaying messages
) {

    //----- Region: Data Initialization -----

    val serviceLocator = LocalServiceLocator.current

    // Viewmodels
    val requestViewModel = serviceLocator.obtainRequestViewModel()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    // Requests get and sort
    val requests by requestViewModel.requests.collectAsState()
    LaunchedEffect(Unit) {
        requestViewModel.getRequests()
    }
    val sortOptions = listOf(
        SortOption("Latest") { r1, r2 -> r2.timestamp.compareTo(r1.timestamp) },
        SortOption("Oldest") { r1, r2 -> r1.timestamp.compareTo(r2.timestamp) }
    )
    var selectedSortOption by remember { mutableStateOf(sortOptions[0]) }
    // Show Add Dialog Variable
    var showAddRequestDialog by remember { mutableStateOf(false) }
    // Coroutine Scope for Snackbar
    val coroutineScope = rememberCoroutineScope()
    // Osserva uno stato del ViewModel per i messaggi Snackbar
    val message by requestViewModel.snackbarMessage.collectAsState(initial = "")
    // Mostra la Snackbar quando il messaggio cambia
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
                requestViewModel.clearSnackbarMessage()
            }
        }
    }

    //----- Region: View Composition -----

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
            // Display based on Resource state
            when(requests){
                // Loading State
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                // Success state
                is Resource.Success -> {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Sort buttons and history button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            SortButtons(
                                sortOptions = sortOptions,
                                selectedSortOption = selectedSortOption,
                                onSortOptionSelected = { selectedSortOption = it }
                            )
                            IconButton(
                                onClick = { navController.navigate(Screens.RequestsHistory.route) },
                                modifier = Modifier.size(38.dp),
                                colors = IconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                                    disabledContentColor = MaterialTheme.colorScheme.onSecondary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Checklist,
                                    contentDescription = "Requests history",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .clip(CircleShape)
                                        .size(150.dp)

                                )
                            }
                        }
                        // Main Component : Requests list
                        RequestList(
                            requests = requests,
                            filterOption = RequestStatus.TODO,
                            sortOption = selectedSortOption,
                            navController = navController,
                            requestViewModel = requestViewModel,
                            homeLessViewModel = homelessViewModel
                        )
                    }
                    // Add Request Floating Button
                    FloatingActionButton(
                        onClick = { showAddRequestDialog = true },
                        elevation = FloatingActionButtonDefaults.elevation(50.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .hapticFeedback(),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Add request", tint = MaterialTheme.colorScheme.onPrimary)
                            Text("Aggiungi Richiesta", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
                // Error state: Show a message and a button to leave the screen
                // This should not be possible.
                is Resource.Error -> {
                    Column {
                        Text("Something went wrong. Please try again later.")
                        Log.e("RequestDetailsScreen", "Error: ${requests.message}")
                        Button(onClick = {
                            navController.popBackStack()
                        }) {
                            Text("Go back")
                        }
                    }
                }
            }

        // Logic to show dialog when the floating button to add is pressed
        if (showAddRequestDialog) {
            AddRequestDialog(
                onDismiss = { showAddRequestDialog = false },
                onAdd = {
                    requestViewModel.addRequest(it)
                    showAddRequestDialog = false
                },
                volunteerViewModel = volunteerViewModel,
                navController = navController,
            )
        }
    }
}
