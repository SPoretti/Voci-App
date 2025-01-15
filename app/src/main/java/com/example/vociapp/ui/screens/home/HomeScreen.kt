package com.example.vociapp.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.core.CustomFAB
import com.example.vociapp.ui.components.core.DrawerContent
import com.example.vociapp.ui.components.core.SearchBar
import com.example.vociapp.ui.components.homeless.CustomHomelessDialog
import com.example.vociapp.ui.components.homeless.HomelessList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    //----- Region: Data Initialization -----

    val serviceLocator = LocalServiceLocator.current
    // Viewmodels
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val mapboxViewModel = serviceLocator.obtainMapboxViewModel()
    // Homeless Data
    val homelesses by homelessViewModel.homelesses.collectAsState()
    val filteredHomelesses by homelessViewModel.filteredHomelesses.collectAsState()
    val searchQuery by homelessViewModel.searchQuery.collectAsState()
    // Display the list of homelesses based on the search query
    val listToDisplay =
        if (searchQuery.isBlank()) {
            homelesses
        } else {
            filteredHomelesses
        }
    // Dialog Variable
    var showAddHomelessDialog by remember { mutableStateOf(false) }
    // PullToRefresh Variables
    val coroutineScope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    val isRefreshing = remember { mutableStateOf(false) }
    // Drawer Variable
    val drawerState = rememberDrawerState(DrawerValue.Closed)
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
    // Initial Setup
    LaunchedEffect(Unit) {
        homelessViewModel.getHomelesses()
        homelessViewModel.updateSearchQuery("")
    }

    //----- Region: View Composition -----
    ModalNavigationDrawer(
        drawerState = drawerState,                          // Drawer state
        drawerContent = { DrawerContent(navController) },   // Drawer content
        gesturesEnabled = true                              // Enable drawer gestures (swipe to open)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Custom Searchbar with leading and trailing icons
                // Leading Icon to open the drawer
                // Trailing Icon to navigate to the user profile
                SearchBar(
                    navController = navController,
                    onLeadingIconClick = { coroutineScope.launch { drawerState.open() } }
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Leading text displaying the number of homelesses in the database
                Text(
                    text = if (listToDisplay.data?.size == 1) {
                        "1 Senzatetto"
                    } else {
                        "${listToDisplay.data?.size ?: 0} Senzatetto"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                // PullToRefreshBox to refresh the data when activated
                PullToRefreshBox(
                    state = refreshState,
                    onRefresh = {
                        isRefreshing.value = true               // Set refreshing state to true
                        // Launch a coroutine to fetch data
                        coroutineScope.launch {
                            serviceLocator.fetchAllData()       // Fetch all data (homelesses, requests, etc.)
                            delay(150)                 // This delay is here because the refresh animation would break if it wasn't
                            homelessViewModel.getHomelesses()   // Refresh the data for this ui
                            isRefreshing.value = false          // Set refreshing state to false
                        }
                    },
                    isRefreshing = isRefreshing.value,
                    indicator = {
                        // Pre built indicator to show when refreshing
                        Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = isRefreshing.value,
                            containerColor = MaterialTheme.colorScheme.primary,
                            color = MaterialTheme.colorScheme.onPrimary,
                            state = refreshState
                        )
                    }
                ) {
                    // Main Content: Display the list of homeless
                    HomelessList(
                        navController = navController,
                    )
                }
            }
            // Custom FAB to add a new homeless
            CustomFAB(
                text = "Aggiungi persona",
                icon = Icons.Filled.Add,
                onClick = { showAddHomelessDialog = true },
                modifier = Modifier.align(Alignment.BottomEnd)
            )
            // AddHomelessDialog to add a new homeless
            if (showAddHomelessDialog) {
                mapboxViewModel.clearLocationVariables()
                CustomHomelessDialog(
                    onDismiss = {
                        showAddHomelessDialog = false
                        mapboxViewModel.clearLocationVariables()
                    },
                    onConfirm = {
                        homelessViewModel.addHomeless(it)
                        showAddHomelessDialog = false
                    },
                    actionText = "Aggiungi"
                )
            }
        }
    }
}