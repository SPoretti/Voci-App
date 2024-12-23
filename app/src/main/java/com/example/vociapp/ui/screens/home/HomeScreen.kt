package com.example.vociapp.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.SearchBar
import com.example.vociapp.ui.components.homeless.AddHomelessDialog
import com.example.vociapp.ui.components.homeless.HomelessList
import com.example.vociapp.ui.components.utils.hapticFeedback
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.getHomelessViewModel()

    val homelesses by homelessViewModel.homelesses.collectAsState()
    val filteredHomelesses by homelessViewModel.filteredHomelesses.collectAsState()
    val searchQuery by homelessViewModel.searchQuery.collectAsState()

    var showAddHomelessDialog by remember { mutableStateOf(false) }

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
                homelessViewModel.clearSnackbarMessage()
            }
        }
    }

    LaunchedEffect(Unit) {
        homelessViewModel.getHomelesses()
        homelessViewModel.updateSearchQuery("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .height(58.dp)
                    ) {
                        SearchBar(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            onSearch = { homelessViewModel.updateSearchQuery(it)},
                            placeholderText = "Cerca Senzatetto...",
                            unfocusedBorderColor = Color.Transparent,
                            onClick = { /* TODO() Handle click on search bar */ },
                            onDismiss = { homelessViewModel.updateSearchQuery("") },
                            navController = navController
                        )
                    }

                    val listToDisplay =
                        if (searchQuery.isBlank()) {
                            homelesses
                        } else {
                            filteredHomelesses
                        }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Senzatetto",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    HomelessList(
                        homelesses = listToDisplay,
                        showPreferredIcon = true,
                        onListItemClick = {homeless ->
                            navController.navigate("profileHomeless/${homeless.name}")
                        },
                        navController = navController,
                        onSwipe = { homeless ->
                            navController.navigate("UpdatesAddScreen/${homeless.id}")
                        }
                    )
                }
            }
        }

        if (showAddHomelessDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ){
                AddHomelessDialog(
                    onDismiss = { showAddHomelessDialog = false },
                    onAdd = {
                        homelessViewModel.addHomeless(it)
                        showAddHomelessDialog = false
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = { showAddHomelessDialog = true },
            elevation = FloatingActionButtonDefaults.elevation(50.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .hapticFeedback(),
            containerColor = MaterialTheme.colorScheme.primary

        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add homeless")
                Text("Aggiungi Senzatetto")
            }
        }
    }
}



