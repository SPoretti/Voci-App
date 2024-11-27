package com.example.vociapp.ui.screens.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.RequestStatus
import com.example.vociapp.data.util.Resource
import com.example.vociapp.data.util.SortOption
import com.example.vociapp.ui.components.RequestCard
import com.example.vociapp.ui.components.SortButtons
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.viewmodels.RequestViewModel


@Composable
fun RequestsScreen(
    navController: NavHostController,
    viewModel: RequestViewModel
) {
    val requests by viewModel.requests.collectAsState()
    var todoRequests = requests.data.orEmpty().filter { it.status == RequestStatus.TODO }
    val sortOptions = listOf(
        SortOption("Latest") { r1, r2 -> r2.timestamp.compareTo(r1.timestamp) },
        SortOption("Oldest") { r1, r2 -> r1.timestamp.compareTo(r2.timestamp) }
    )
    var selectedSortOption by remember { mutableStateOf(sortOptions[0]) }

    LaunchedEffect(Unit) {
        viewModel.getRequests()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        SortButtons(
            sortOptions = sortOptions,
            selectedSortOption = selectedSortOption,
            onSortOptionSelected = { selectedSortOption = it }
        )

        Column (
            modifier = Modifier.fillMaxWidth()
        ){

            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)) {
                // History button
                IconButton(
                    onClick = { navController.navigate(Screens.RequestsHistory.route) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = "Requests history",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(6.dp)
                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clip(CircleShape)
                            .padding(4.dp)
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
                            Text("Non ci sono richieste attive", modifier = Modifier.align(Alignment.Center))
                        } else {
                            todoRequests = todoRequests.sortedWith(selectedSortOption.comparator)
                            LazyColumn {
                                items(todoRequests) { request ->
                                    RequestCard(request = request)
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Text(text = "Error: ${requests.message}", modifier = Modifier.align(Alignment.Center))
                    }
                }
            }

        }

        Button(
            onClick = { navController.navigate(Screens.AddRequest.route) },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(18.dp)
        ) {
            Text(text = "Aggiungi")
        }

    }
}