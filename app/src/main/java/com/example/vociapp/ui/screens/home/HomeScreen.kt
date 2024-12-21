package com.example.vociapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
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
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.homeless.AddHomelessDialog
import com.example.vociapp.ui.components.homeless.HomelessList
import com.example.vociapp.ui.components.SearchBar
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

            Spacer(modifier = Modifier.height(8.dp))

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
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SearchBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            onSearch = { homelessViewModel.updateSearchQuery(it)},
                            placeholderText = "Cerca...",
                            unfocusedBorderColor = Color.Transparent,
                            onClick = { /* TODO() Handle click on search bar */ },
                            onDismiss = { homelessViewModel.updateSearchQuery("") }
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Button(
                            onClick = {showAddHomelessDialog = true},
                            modifier = Modifier
                                .size(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add homeless",
                                    modifier = Modifier.size(28.dp)
                                )
                            },
                            contentPadding = PaddingValues(8.dp),
                            shape = CircleShape
                        )
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    val listToDisplay =
                        if (searchQuery.isBlank()) {
                            homelesses
                        } else {
                            filteredHomelesses
                        }

                    HomelessList(
                        homelesses = listToDisplay,
                        showPreferredIcon = true,
                        onListItemClick = {homeless ->
                            navController.navigate("profileHomeless/${homeless.name}")
                        },
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
    }
}



