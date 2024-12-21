package com.example.vociapp.ui.components.homeless

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vociapp.data.types.AuthState
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.state.HomelessItemUiState

@Composable
fun HomelessList(
    homelesses: Resource<List<Homeless>>,
    showPreferredIcon: Boolean,
    onListItemClick: (Homeless) -> Unit = {},
    selectedHomeless: Homeless? = null,
    modifier: Modifier = Modifier
) {

    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.getHomelessViewModel()
    val volunteerViewModel = serviceLocator.getVolunteerViewModel()
    val authViewModel = serviceLocator.getAuthViewModel()
    val isLoggedIn by authViewModel.authState.collectAsState()

    val userId by remember {mutableStateOf(volunteerViewModel.currentUser.value?.id)}
    val userPreferences by volunteerViewModel.userPreferencesResource.collectAsState()

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
                            .sortedByDescending { userPreferences.data?.contains(it.id) ?: false }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(sortedHomelessList) { homeless ->
                                HomelessListItem(
                                    homelessState = HomelessItemUiState(
                                        homeless = homeless
                                    ),
                                    showPreferredIcon = showPreferredIcon,
                                    onClick = onListItemClick,
                                    isSelected = (homeless.id == selectedHomeless?.id)
                                )
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