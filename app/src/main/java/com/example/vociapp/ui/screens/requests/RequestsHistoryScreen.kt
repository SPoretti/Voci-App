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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.RequestStatus
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.requests.RequestList
import com.example.vociapp.ui.components.requests.SortButtons
import com.example.vociapp.ui.components.requests.SortOption

@Composable
fun RequestsHistoryScreen(
    navController: NavHostController    // Navigation controller for navigation
) {
    //----- Region: Data Initialization -----

    val serviceLocator = LocalServiceLocator.current
    // Viewmodels
    val requestViewModel = serviceLocator.obtainRequestViewModel()
    // Requests get and sort
    val completedRequests by requestViewModel.completedRequests.collectAsState()
    LaunchedEffect(Unit) {
        requestViewModel.getCompletedRequests()
    }
    val sortOptions = listOf(
        SortOption("Latest") { r1, r2 -> r2.timestamp.compareTo(r1.timestamp) },
        SortOption("Oldest") { r1, r2 -> r1.timestamp.compareTo(r2.timestamp) }
    )
    var selectedSortOption by remember { mutableStateOf(sortOptions[0]) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)

    ) {
        // Display based on Resource state
        when(completedRequests){
            // Loading State
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            // Success State
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

                    }
                    // Main Component : Requests list
                    RequestList(
                        requests = completedRequests,
                        filterOption = RequestStatus.DONE,
                        sortOption = selectedSortOption,
                        navController = navController
                    )
                }
            }
            // Error state: Show a message and a button to leave the screen
            // This should not be possible.
            is Resource.Error -> {
                Column {
                    Text("Something went wrong. Please try again later.")
                    Log.e("RequestDetailsScreen", "Error: ${completedRequests.message}")
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