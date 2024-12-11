package com.example.vociapp.ui.screens.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.RequestStatus
import com.example.vociapp.data.util.SortOption
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.requests.RequestDetails
import com.example.vociapp.ui.components.requests.RequestList
import com.example.vociapp.ui.components.SortButtons

@Composable
fun RequestsHistoryScreen(
    navController: NavHostController
) {

    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.getHomelessViewModel()
    val requestViewModel = serviceLocator.getRequestViewModel()
    val requests by requestViewModel.requests.collectAsState()


    val sortOptions = listOf(
        SortOption("Latest") { r1, r2 -> r2.timestamp.compareTo(r1.timestamp) },
        SortOption("Oldest") { r1, r2 -> r1.timestamp.compareTo(r2.timestamp) }
    )
    var selectedSortOption by remember { mutableStateOf(sortOptions[0]) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedRequest: Request by remember {
        mutableStateOf(
            Request(
                id = "null",
                title = "",
                description = "",
                timestamp = 0,
                creatorId = "",
                homelessID = "",
            )
        )
    }

    LaunchedEffect(Unit) {
        requestViewModel.getRequests()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)

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

            }

            RequestList(
                requests = requests,
                filterOption = RequestStatus.DONE,
                sortOption = selectedSortOption,
                navController = navController,
                requestViewModel = requestViewModel,
                homeLessViewModel = homelessViewModel
            )

        }

        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false }
            ) {
                RequestDetails(
                    request = selectedRequest,
                    onDismiss = { showDialog = false },
                    navController = navController,
                    homelessViewModel = homelessViewModel
                )
            }
        }

    }
}