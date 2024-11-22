package com.example.vociapp.ui.screens.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.model.RequestStatus
import com.example.vociapp.data.model.RequestTab
import com.example.vociapp.data.util.Resource
import com.example.vociapp.ui.components.RequestCard
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.viewmodels.RequestViewModel


@Composable
fun RequestsScreen(
    navController: NavHostController,
    viewModel: RequestViewModel
) {
    val requests by viewModel.requests.collectAsState()
    val tabs = listOf(RequestTab.ToDo, RequestTab.Done)
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getRequests()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(tab.title) }
                    )
                }
            }


            Box(modifier = Modifier.fillMaxSize()) {

                when (requests) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is Resource.Success -> {
                        val filteredRequests = when (tabs[selectedTabIndex]) {
                            RequestTab.ToDo -> requests.data.orEmpty().filter { it.status == RequestStatus.TODO }
                            RequestTab.Done -> requests.data.orEmpty().filter { it.status == RequestStatus.DONE }
                        }

                        if (filteredRequests.isEmpty()) {
                            Text("Non ci sono richieste", modifier = Modifier.align(Alignment.Center))
                        } else {
                            LazyColumn {
                                items(filteredRequests) { request ->
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

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Button(
                onClick = { navController.navigate(Screens.AddRequest.route) },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            ) {
                Text(text = "Aggiungi")
            }
        }

    }
}