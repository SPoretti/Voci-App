package com.example.vociapp.ui.components.homeless

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.types.AuthState
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.navigation.currentRoute
import com.example.vociapp.ui.state.HomelessItemUiState

@Composable
fun HomelessList(
    homelesses: Resource<List<Homeless>>,
    showPreferredIcon: Boolean,
    onListItemClick: (Homeless) -> Unit = {},
    selectedHomeless: Homeless? = null,
    onSwipe: (Homeless) -> Unit = {},
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()
    val authViewModel = serviceLocator.obtainAuthViewModel()
    val isLoggedIn by authViewModel.authState.collectAsState()

    val userId by remember {mutableStateOf(volunteerViewModel.currentUser.value.data?.id)}
    val userPreferences by volunteerViewModel.userPreferencesResource.collectAsState()

    val currentRoute = currentRoute(navController = navController)

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn is AuthState.Authenticated){
            homelessViewModel.getHomelesses()
            if (userId != null) {
                volunteerViewModel.fetchUserPreferences(userId!!)
            }
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {

        when (homelesses) {
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            is Resource.Success -> {
                when (userPreferences) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    is Resource.Success -> {
                        val sortedHomelessList = homelesses.data.orEmpty()
                            .sortedByDescending { userPreferences.data?.contains(it.id) == true }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if(currentRoute == "home") {

                                items(sortedHomelessList) { homeless ->
                                    val view = LocalView.current
                                    val density = LocalDensity.current
                                    val swipeState = remember(density) {
                                        SwipeToDismissBoxState(
                                            initialValue = SwipeToDismissBoxValue.Settled,
                                            density = density,
                                            confirmValueChange = { newValue ->
                                                newValue == SwipeToDismissBoxValue.StartToEnd
                                            },
                                            positionalThreshold = { totalDistance ->
                                                totalDistance * 0.4f
                                            }
                                        )
                                    }

                                    LaunchedEffect(swipeState.currentValue) {
                                        if (swipeState.currentValue != SwipeToDismissBoxValue.Settled) {
                                            view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                                        }
                                        if (swipeState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
                                            onSwipe(homeless)
                                        }
                                    }

                                    SwipeToDismissBox(
                                        state = swipeState,
                                        backgroundContent = {
                                            // Background content when swiping (e.g., delete icon)
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(shape = RoundedCornerShape(16.dp))
                                                    .background(MaterialTheme.colorScheme.secondary),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Spacer(modifier = Modifier.width(16.dp))
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.Comment,
                                                    contentDescription = "Comment Update",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Aggiorna", color = Color.White)
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        enableDismissFromStartToEnd = true,
                                        enableDismissFromEndToStart = false,
                                        gesturesEnabled = true
                                    ) {
                                        // Main content (HomelessListItem)
                                        HomelessListItem(
                                            homelessState = HomelessItemUiState(homeless = homeless),
                                            showPreferredIcon = showPreferredIcon,
                                            onClick = onListItemClick,
                                            isSelected = (homeless.id == selectedHomeless?.id)
                                        )
                                    }
                                }
                            } else {
                                items(sortedHomelessList) { homeless ->
                                    HomelessListItem(
                                        homelessState = HomelessItemUiState(homeless = homeless),
                                        showPreferredIcon = showPreferredIcon,
                                        onClick = onListItemClick,
                                        isSelected = (homeless.id == selectedHomeless?.id)
                                    )
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Text(
                            text = "Error: ${userPreferences.message}",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            is Resource.Error -> {
                Text(
                    text = "Error: ${homelesses.message}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}