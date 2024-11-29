package com.example.vociapp.ui.screens.homelesses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.ui.components.HomelessForm
import com.example.vociapp.ui.viewmodels.AuthViewModel
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import kotlinx.coroutines.launch

@Composable
fun AddHomelessScreen(
    navController: NavHostController,
    homelessViewModel: HomelessViewModel,
    authViewModel: AuthViewModel,

    ) {

    val addHomelessState by homelessViewModel.homelesses.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Osserva uno stato del ViewModel per i messaggi Snackbar
    val message by homelessViewModel.snackbarMessage.collectAsState(initial = "")

    // Mostra la Snackbar quando il messaggio cambia
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
                homelessViewModel.clearSnackbarMessage() // Reset dello stato dopo aver mostrato
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ){ padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HomelessForm(
                onAddItemClick = { homeless ->
                    homelessViewModel.addHomeless(homeless) // Call the ViewModel's addHomeless function
                    navController.popBackStack() // Navigate back after adding
                },
                navController = navController,
                authViewModel = authViewModel,
            )
        }
    }



}