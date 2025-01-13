package com.example.vociapp.ui.components.updates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.util.Resource

@Composable
fun UpdateList(updates: Resource<List<Update>>) {

    val sortedUpdates = remember(updates) {
        updates.data.orEmpty().sortedByDescending { it.timestamp } // Sort by timestamp descending
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (updates) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is Resource.Success -> {
                if (sortedUpdates.isEmpty()) {
                    Text(
                        text = "Non ci sono aggiornamenti",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
                    ) {
                        items(sortedUpdates) { update ->
                            UpdateListItem(update)
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