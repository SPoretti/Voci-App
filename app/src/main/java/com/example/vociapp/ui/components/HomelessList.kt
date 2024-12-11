package com.example.vociapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.state.HomelessItemUiState
import com.example.vociapp.ui.viewmodels.HomelessViewModel

@Composable
fun HomelessList(
    homelesses: Resource<List<Homeless>>,
    homelessViewModel: HomelessViewModel,
    navController: NavHostController,
    showPreferredIcon: Boolean,
    onListItemClick: (Homeless) -> Unit = {},
    selectedHomeless: Homeless? = null,
    modifier: Modifier = Modifier
) {

    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.getAuthViewModel()
    val homelessViewModel = serviceLocator.getHomelessViewModel()
    val volunteerViewModel = serviceLocator.getVolunteerViewModel()
    val userId = volunteerViewModel.specificVolunteer.value.data?.id ?: ""
    //val homelessState by remember { mutableStateOf(HomelessItemUiState(Homeless())) }
    val userPreferences by volunteerViewModel.userPreferences.collectAsState()


    LaunchedEffect(Unit) {
        homelessViewModel.getHomelesses()
    }

//    LaunchedEffect(key1 = homelessState) {
//        homelessState = homelessState.copy(
//            isPreferred = volunteerViewModel.isPreferred(userId, homelessState.homeless.id)
//        )
//    }

    Box(modifier = modifier.fillMaxWidth()) {

        when (homelesses) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is Resource.Success -> {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(homelesses.data.orEmpty()) { homeless ->
                        val isPreferred = userPreferences?.contains(homeless.id) ?: false
                        val homelessState by remember { mutableStateOf(HomelessItemUiState(homeless = homeless, isPreferred = isPreferred)) }

                        HomelessListItem(
                            homelessState = homelessState,
                            showPreferredIcon = showPreferredIcon,
                            onClick = onListItemClick,
                            isSelected = (homeless.id == selectedHomeless?.id)
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