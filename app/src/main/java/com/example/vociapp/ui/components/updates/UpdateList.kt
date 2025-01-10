package com.example.vociapp.ui.components.updates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.util.Resource
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.example.vociapp.ui.viewmodels.UpdatesViewModel

@Composable
fun UpdateList(
    updates: Resource<List<Update>>,
    navController: NavHostController,
    updateViewModel: UpdatesViewModel,
    homelessViewModel: HomelessViewModel,
) {

    val Updates = remember(updates) {
        updates.data.orEmpty().sortedByDescending { it.timestamp } // Sort by timestamp descending
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        when (updates) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is Resource.Success -> {
                if (Updates.isEmpty()) {
                    Text(
                        "Non ci sono aggiornamenti",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
                    ) {
                        items(Updates) { update ->
                            UpdateListItem(update, navController, homelessViewModel)
                        }
                    }
                }
            }

            is Resource.Error -> {
                Text(
                    text = "Error: ${updates.message}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}