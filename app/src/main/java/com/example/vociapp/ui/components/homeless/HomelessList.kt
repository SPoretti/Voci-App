package com.example.vociapp.ui.components.homeless

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.util.Resource
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

    LaunchedEffect(Unit) {
        homelessViewModel.getHomelesses()
    }

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
                        HomelessListItem(
                            homeless = homeless,
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