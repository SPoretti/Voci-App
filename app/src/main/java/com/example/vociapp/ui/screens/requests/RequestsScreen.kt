package com.example.vociapp.ui.screens.requests

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.RequestStatus
import com.example.vociapp.data.util.Resource
import com.example.vociapp.data.util.SortOption
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.AddRequestDialog
import com.example.vociapp.ui.components.RequestDetails
import com.example.vociapp.ui.components.RequestListItem
import com.example.vociapp.ui.components.SortButtons
import com.example.vociapp.ui.navigation.Screens
import kotlinx.coroutines.launch


@Composable
fun RequestsScreen(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val requestViewModel = serviceLocator.getRequestViewModel()
    val authViewModel = serviceLocator.getAuthViewModel()

    val requests by requestViewModel.requests.collectAsState()
    var todoRequests = requests.data.orEmpty().filter { it.status == RequestStatus.TODO }
    val sortOptions = listOf(
        SortOption("Latest") { r1, r2 -> r2.timestamp.compareTo(r1.timestamp) },
        SortOption("Oldest") { r1, r2 -> r1.timestamp.compareTo(r2.timestamp) }
    )
    var selectedSortOption by remember { mutableStateOf(sortOptions[0]) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedRequest: Request by remember { mutableStateOf(Request(id = "null", title = "", description = "", timestamp = 0)) }

    var showAddRequestDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
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
                requestViewModel.clearSnackbarMessage() // Reset dello stato dopo aver mostrato
            }
        }
    }

    LaunchedEffect(Unit) {
        requestViewModel.getRequests()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ){ padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

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

                    // History button
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
                            imageVector = Icons.Filled.History,
                            contentDescription = "Requests history",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(6.dp)
                                .clip(CircleShape)
                                .size(150.dp)

                        )
                    }
                }


                Box(modifier = Modifier.fillMaxWidth()) {

                    when (requests) {
                        is Resource.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                        is Resource.Success -> {
                            if (todoRequests.isEmpty()) {
                                Text(
                                    "Non ci sono richieste attive",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            } else {
                                todoRequests =
                                    todoRequests.sortedWith(selectedSortOption.comparator)
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(1),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(todoRequests) { request ->
                                        // SwipeToDismissBoxState with necessary configuration
                                        val density = LocalDensity.current
                                        val swipeState = remember(density) {
                                            SwipeToDismissBoxState(
                                                initialValue = SwipeToDismissBoxValue.Settled, // Initial state
                                                density = density, // Provides density for thresholds
                                                confirmValueChange = { newValue ->
                                                    // Confirm dismissal only for the Dismissed state
                                                    newValue == SwipeToDismissBoxValue.StartToEnd
                                                },
                                                positionalThreshold = { totalDistance ->
                                                    // Set a threshold to trigger dismissal (e.g., 50% of the distance)
                                                    totalDistance * 0.2f
                                                }
                                            )
                                        }

                                        // Observe dismissal state
                                        LaunchedEffect(swipeState.currentValue) {
                                            if (swipeState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
                                                requestViewModel.requestDone(request)
                                            }
                                        }

                                        // SwipeToDismissBox with swipe gestures
                                        SwipeToDismissBox(
                                            state = swipeState,

                                            backgroundContent = {
                                                // Optional background content while swiping (e.g., delete icon)
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .clip(shape = RoundedCornerShape(16.dp))
                                                        .background(MaterialTheme.colorScheme.primary),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Spacer(modifier = Modifier.width(16.dp))
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Delete",
                                                        tint = Color.White
                                                    )
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            enableDismissFromStartToEnd = true, // Allow swipe from start to end
                                            enableDismissFromEndToStart = false, // Allow swipe from end to start
                                            gesturesEnabled = true // Enable gestures
                                        ) {
                                            // Main content inside the swipe box
                                            RequestListItem(
                                                request = request,
                                                navController,
                                                requestViewModel
                                            ) {
                                                showDialog = true
                                                selectedRequest = request
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                }

                            }
                        }

                        is Resource.Error -> {
                            Text(
                                text = "Error: ${requests.message}",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                    }
                }

            }

            FloatingActionButton(
                onClick = { showAddRequestDialog = true },
                elevation = FloatingActionButtonDefaults.elevation(50.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary

            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add request")
            }

            if (showDialog) {
                Dialog(
                    onDismissRequest = { showDialog = false }
                ) {
                    RequestDetails(
                        request = selectedRequest,
                        onDismiss = { showDialog = false },
                        navController
                    )
                }
            }

            if (showAddRequestDialog) {
                AddRequestDialog(
                    onDismiss = { showAddRequestDialog = false },
                    onAdd = {
                        requestViewModel.addRequest(it)
                        showAddRequestDialog = false
                    },
                    authViewModel = authViewModel,
                )
            }

        }
    }
}
