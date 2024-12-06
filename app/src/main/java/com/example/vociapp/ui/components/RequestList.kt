package com.example.vociapp.ui.components

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
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.RequestStatus
import com.example.vociapp.data.util.Resource
import com.example.vociapp.data.util.SortOption
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.example.vociapp.ui.viewmodels.RequestViewModel

@Composable
fun RequestList(
    requests: Resource<List<Request>>,
    filterOption: RequestStatus,
    sortOption: SortOption,
    onItemClick: (Request) -> Unit,
    navController: NavHostController,
    requestViewModel: RequestViewModel,
    homeLessViewModel: HomelessViewModel,
) {
    val filteredRequests = remember(requests, sortOption) { // Remember to avoid recalculation
        requests.data.orEmpty().filter { it.status == filterOption }.sortedWith(sortOption.comparator)
    }

    Box{
        when (requests) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is Resource.Success -> {
                if (filteredRequests.isEmpty()) {
                    Text(
                        "Non ci sono richieste",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredRequests) { request ->
                            RequestListItem(
                                request = request,
                                navController = navController,
                                requestViewModel = requestViewModel,
                                homelessViewModel = homeLessViewModel
                            ) {
                                onItemClick(request)
                            }
                        }
                    }
                }
            }

            is Resource.Error -> {
                Text(
                    text = "Error: ${requests.message}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
