package com.example.vociapp.ui.screens.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.RequestStatus
import com.example.vociapp.data.util.Resource
import com.example.vociapp.data.util.SortOption
import com.example.vociapp.ui.components.RequestCard
import com.example.vociapp.ui.components.RequestDetails
import com.example.vociapp.ui.components.RequestListItem
import com.example.vociapp.ui.components.SortButtons
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.viewmodels.RequestViewModel

@Composable
fun RequestsHistoryScreen(navController: NavHostController, viewModel: RequestViewModel) {

    val requests by viewModel.requests.collectAsState()
    var doneRequests = requests.data.orEmpty().filter { it.status == RequestStatus.DONE }

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
                timestamp = 0
            )
        )
    }


    LaunchedEffect(Unit) {
        viewModel.getRequests()
    }

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
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
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
                        if (doneRequests.isEmpty()) {
                            Text(
                                "Non ci sono richieste attive",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            doneRequests = doneRequests.sortedWith(selectedSortOption.comparator)
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(1),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(doneRequests) { request ->
                                    RequestListItem(
                                        request = request, navController, viewModel
                                    ) {
                                        showDialog = true
                                        selectedRequest = request
                                    }
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

    }
}