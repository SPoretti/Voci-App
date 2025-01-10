package com.example.vociapp.ui.components.requests

import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.RequestStatus
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.state.SortOption

@Composable
fun RequestList(
    requests: Resource<List<Request>>,      // List of requests
    filterOption: RequestStatus,            // Filter option
    sortOption: SortOption,                 // Sort option
    navController: NavHostController,       // Navigation controller
) {
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    // Viewmodels
    val requestViewModel = serviceLocator.obtainRequestViewModel()
    // Filtered requests based on filter and sort options
    val filteredRequests = remember(requests, sortOption) {
        requests.data.orEmpty().filter { it.status == filterOption }.sortedWith(sortOption.comparator)
    }
    //----- Region: View Composition -----
    Box(modifier = Modifier.fillMaxWidth()) {
        // Display based on Resource status
        when (requests) {
            // Loading state
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            // Success State
            is Resource.Success -> {
                // If there are no requests, display a message
                if (filteredRequests.isEmpty()) {
                    Text(
                        "Non ci sono richieste attive.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else { // Display the list of requests
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),       // One column
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Iterate through the filtered requests and display them
                        items(filteredRequests) { request ->
                            // SwipeToDismissBoxState with necessary configuration
                            val view = LocalView.current        // Get local view for haptic feedback
                            val density = LocalDensity.current  // Get local density for swipe gesture
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
                                        totalDistance * 0.4f
                                    }
                                )
                            }

                            // Observe dismissal state
                            LaunchedEffect(swipeState.currentValue) {
                                // Perform haptic feedback on dismissal
                                if (swipeState.currentValue != SwipeToDismissBoxValue.Settled) {
                                    view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                                }
                                // Perform action based on the swipe direction (From left to right)
                                if (swipeState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
                                    // Requests page
                                    if(request.status == RequestStatus.TODO){
                                        // Swipe to complete request
                                        requestViewModel.requestDone(request)
                                    }
                                    // History page
                                    else {
                                        requestViewModel.deleteRequest(request)
                                    }
                                }
                            }

                            // SwipeToDismissBox with swipe gestures
                            SwipeToDismissBox(
                                state = swipeState, // SwipeToDismissBoxState
                                backgroundContent = {
                                    // Home page
                                    if(filterOption == RequestStatus.TODO){
                                        // Swipe to complete request (bg: Blue, icon: Check)
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
                                                contentDescription = "Done",
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .size(32.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Completata", color = Color.White)
                                        }
                                    }
                                    // History page
                                    if(filterOption == RequestStatus.DONE){
                                        // Swipe to delete request (bg: Red, icon: Delete)
                                        Row(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(shape = RoundedCornerShape(16.dp))
                                                .background(MaterialTheme.colorScheme.error),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .size(32.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Elimina", color = Color.White)
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .fillMaxWidth(),
                                enableDismissFromStartToEnd = true,     // Allow swipe from left to right
                                enableDismissFromEndToStart = false,    // Don't allow swipe from right to left
                                gesturesEnabled = true                  // Enable gestures
                            ) {
                                // Main content inside the swipe box
                                RequestListItem(
                                    request = request,
                                    navController
                                )
                            }
                        }
                    }
                }
            }
            // Error state
            is Resource.Error -> {
                Log.e("RequestDetailsScreen", "Error: ${requests.message}")
                Column {
                    Text("Something went wrong. Please try again later.")
                    Button(onClick = {
                        navController.popBackStack()
                    }) {
                        Text("Go back")
                    }
                }
            }
        }
    }
}
