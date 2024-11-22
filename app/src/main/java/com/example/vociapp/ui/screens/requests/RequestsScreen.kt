package com.example.vociapp.ui.screens.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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

    LaunchedEffect(Unit) {
        viewModel.getRequests()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (requests) {
            is Resource.Loading -> {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                // Display the list of requests using LazyColumn
                LazyColumn {
                    items(requests.data!!) { request ->
                        RequestCard(request = request) // Pass the request to RequestCard
                    }
                }
            }
            is Resource.Error -> {
                // Display an error message
                Text(text = "Error: ${(requests as Resource.Error).message}")
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
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