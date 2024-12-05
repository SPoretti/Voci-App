package com.example.vociapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.R
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.AddHomelessDialog
import com.example.vociapp.ui.components.HomelessList
import com.example.vociapp.ui.components.SearchBar
import kotlinx.coroutines.launch
import kotlin.text.isBlank

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.getHomelessViewModel()

    val homelesses by homelessViewModel.homelesses.collectAsState()
    val filteredHomelesses by homelessViewModel.filteredHomelesses.collectAsState()
    val searchQuery by homelessViewModel.searchQuery.collectAsState()

    var showAddHomelessDialog by remember { mutableStateOf(false) }

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

    LaunchedEffect(Unit) {
        homelessViewModel.getHomelesses()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ){ padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.voci_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = 0.dp,
                                bottom = 8.dp,
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            SearchBar(
                                modifier = Modifier.fillMaxWidth(),
                                onSearch = { homelessViewModel.updateSearchQuery(it)},
                                placeholderText = "Cerca...",
                                unfocusedBorderColor = Color.Transparent,
                            )
                        }

                        Spacer(modifier = Modifier.padding(8.dp))

                        val listToDisplay = if (searchQuery.isBlank()) {
                            homelesses
                        } else {
                            filteredHomelesses
                        }

                        HomelessList(
                            homelesses = listToDisplay,
                            homelessViewModel = homelessViewModel,
                            navController = navController,
                            showPreferredIcon = true,
                            onListItemClick = {homeless ->
                                navController.navigate("profileHomeless/${homeless.name}")
                            },
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = {showAddHomelessDialog = true},
                elevation = FloatingActionButtonDefaults.elevation(50.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add homeless")
            }

            if (showAddHomelessDialog) {
                AddHomelessDialog(
                    onDismiss = { showAddHomelessDialog = false },
                    onAdd = {
                        homelessViewModel.addHomeless(it)
                        showAddHomelessDialog = false
                    }
                )
            }
        }
    }

    fun navigateToProfile(homeless: Homeless) {
        navController.navigate("profileHomeless/${homeless.name}")
    }

}



