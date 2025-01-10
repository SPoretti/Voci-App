package com.example.vociapp.ui.components.homeless

import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.state.HomelessItemUiState

@Composable
fun HomelessList(
    navController: NavController    // Navigation controller for navigation
) {
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    // Viewmodels
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()
    // Homeless Data
    val homelesses by homelessViewModel.homelesses.collectAsState()
    val filteredHomelesses by homelessViewModel.filteredHomelesses.collectAsState()
    val searchQuery by homelessViewModel.searchQuery.collectAsState()
    // Display the list of homelesses based on the search query
    val listToDisplay =
        if (searchQuery.isBlank()) {
            homelesses
        } else {
            filteredHomelesses
        }
    // Get logged user to get preferences
    val user by remember { mutableStateOf(volunteerViewModel.currentUser) }

    //----- Region: View Composition -----
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Display based on Resource State
        when (listToDisplay) {
            // Loading State
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            // Success State
            is Resource.Success -> {
                // Display favorites on top
                val sortedHomelessList = listToDisplay.data.orEmpty()
                    .sortedByDescending { user.value?.preferredHomelessIds?.contains(it.id) }
                // Display List Wrapper
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Display each homeless in the list
                    items(sortedHomelessList) { homeless ->
                        // Swipe To Dismiss Box State Variables
                        val view = LocalView.current
                        val density = LocalDensity.current
                        val swipeState = remember(density) {
                            SwipeToDismissBoxState(
                                initialValue = SwipeToDismissBoxValue.Settled,
                                density = density,
                                confirmValueChange = { newValue ->
                                    newValue == SwipeToDismissBoxValue.StartToEnd
                                },
                                positionalThreshold = { totalDistance ->
                                    totalDistance * 0.4f
                                }
                            )
                        }
                        // Swipe Gesture Logic
                        LaunchedEffect(swipeState.currentValue) {
                            if (swipeState.currentValue != SwipeToDismissBoxValue.Settled) {
                                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                            }
                            if (swipeState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
                                navController.navigate("UpdatesAddScreen/${homeless.id}")
                            }
                        }
                        // Swipe To Dismiss Box
                        SwipeToDismissBox(
                            state = swipeState,
                            backgroundContent = {
                                // Background content revealed when swiping (bg: orange, icon: Comment)
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(shape = RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.secondary),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Comment,
                                        contentDescription = "Comment Update",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Aggiornamento", color = Color.White)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enableDismissFromStartToEnd = true,         // Enable from left to right
                            enableDismissFromEndToStart = false,        // Disable from right to left
                            gesturesEnabled = true                      // Enable gestures
                        ) {
                            // Main content (HomelessListItem)
                            HomelessListItem(
                                homelessState = HomelessItemUiState(homeless = homeless),
                                showPreferredIcon = true,
                                onClick = { homeless ->
                                    navController.navigate("profileHomeless/${homeless.id}")
                                },
                                onChipClick = {
                                    navController.navigate("HomelessesMap/${homeless.id}")
                                }
                            )
                        }
                    }
                }
            }
            // Error state
            is Resource.Error -> {
                Log.e("RequestDetailsScreen", "Error: ${listToDisplay.message}")
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