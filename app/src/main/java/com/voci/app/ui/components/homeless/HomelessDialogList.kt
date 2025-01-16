package com.voci.app.ui.components.homeless

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.voci.app.data.local.database.Homeless
import com.voci.app.data.util.Resource
import com.voci.app.di.LocalServiceLocator

@Composable
fun HomelessDialogList(
    onListItemClick: (Homeless) -> Unit = {},   // Callback to handle item click
    navController: NavController                // Navigation controller for navigation
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
    // Preferences
    val preferences by volunteerViewModel.preferences.collectAsState()
    // Display the list of homelesses based on the search query
    val listToDisplay =
        if (searchQuery.isBlank()) {
            homelesses
        } else {
            filteredHomelesses
        }

    //----- Region: View Composition -----
    Box(modifier = Modifier.fillMaxWidth()) {
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
                val sortedHomelessList = listToDisplay.data.orEmpty().sortedByDescending { homeless ->
                    // Check if the current user has a preference for this homeless person
                    val isPreferred = preferences.let { resource ->
                        if (resource is Resource.Success) {
                            resource.data?.any { it.homelessId == homeless.id } ?: false
                        } else {
                            false
                        }
                    }
                    isPreferred
                }
                // Display List Wrapper
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Display each homeless in the list
                    items(sortedHomelessList) { homeless ->
                        HomelessListItem(
                            homelessState = HomelessItemUiState(homeless = homeless),
                            showPreferredIcon = false,
                            onClick = onListItemClick,
                            onChipClick = { onListItemClick }
                        )
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