package com.example.vociapp.ui.components.requests

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.RequestStatus
import com.example.vociapp.data.util.Resource
import com.example.vociapp.data.util.SortOption
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.example.vociapp.ui.viewmodels.RequestViewModel

@Composable
fun RequestList(
    requests: Resource<List<Request>>,
    filterOption: RequestStatus,
    sortOption: SortOption,
    navController: NavHostController,
    requestViewModel: RequestViewModel,
    homeLessViewModel: HomelessViewModel,
) {
    val filteredRequests = remember(requests, sortOption) {
        requests.data.orEmpty().filter { it.status == filterOption }.sortedWith(sortOption.comparator)
    }

    Box(modifier = Modifier.fillMaxWidth()) {

        when (requests) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is Resource.Success -> {
                if (filteredRequests.isEmpty()) {
                    Text(
                        "Non ci sono richieste attive",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredRequests) { request ->
                            // SwipeToDismissBoxState with necessary configuration
                            val view = LocalView.current
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
                                if (swipeState.currentValue != SwipeToDismissBoxValue.Settled) {
                                    view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                                }
                                if (swipeState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
                                    if(request.status == RequestStatus.TODO){
                                        requestViewModel.requestDone(request)
                                    }
                                    else {
                                        requestViewModel.deleteRequest(request)
                                    }
                                }
                            }

                            // SwipeToDismissBox with swipe gestures
                            SwipeToDismissBox(
                                state = swipeState,

                                backgroundContent = {
                                    if(filterOption == RequestStatus.TODO){
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
                                                contentDescription = "Check",
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .size(32.dp)
                                            )
                                        }
                                    }
                                    if(filterOption == RequestStatus.DONE){
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
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                enableDismissFromStartToEnd = true, // Allow swipe from start to end
                                enableDismissFromEndToStart = false, // Allow swipe from end to start
                                gesturesEnabled = true // Enable gestures
                            ) {
                                // Main content inside the swipe box
                                RequestListItem(
                                    request = request,
                                    navController,
                                    homeLessViewModel
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
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
