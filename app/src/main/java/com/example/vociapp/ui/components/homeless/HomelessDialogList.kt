package com.example.vociapp.ui.components.homeless

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.state.HomelessItemUiState

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
                val sortedHomelessList = listToDisplay.data.orEmpty()
                    .sortedByDescending { user.value?.preferredHomelessIds?.contains(it.id) }
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