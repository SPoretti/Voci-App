package com.example.vociapp.ui.components.homeless

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.UUID
import kotlin.collections.orEmpty

@Composable
fun AddLocationSearchbar (
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val sessionToken = UUID.randomUUID().toString()

    var searchText by remember { mutableStateOf("") }
    val suggestedLocations by homelessViewModel.suggestedLocations.collectAsState()

    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationHandler = remember {
        LocationHandler(context, fusedLocationClient, homelessViewModel)
    }

    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    LaunchedEffect(Unit) {
        locationHandler.getCurrentLocation { location ->
            currentLocation = location
        }
    }

    Column(
        modifier = modifier
    ) {
        TextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
                homelessViewModel.fetchSuggestions(
                    query = newText,
                    sessionToken = sessionToken,
                    proximity = "${currentLocation?.second},${currentLocation?.first}"
                )
                Log.d("ApiTesting", newText)
            },
            placeholder = {
                Text(
                    text = "Cerca...",
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = Color.Transparent,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(35.dp),
            leadingIcon = {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) { // If the search query is not empty show an X to clear it
                    Row(
                        modifier = Modifier
                            .padding(end = 12.dp)
                    ) {
                        IconButton(
                            onClick = {
                                searchText = ""
                                homelessViewModel.fetchSuggestions(
                                    query = searchText,
                                    sessionToken = sessionToken,
                                    proximity = "${currentLocation?.second},${currentLocation?.first}"
                                )
                                Log.d("ApiTesting", searchText)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Account",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(32.dp)
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        when (suggestedLocations) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                val sortedSuggestions = suggestedLocations.data.orEmpty()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .clip(shape = RoundedCornerShape(16.dp))
                ) {
                    sortedSuggestions.forEach { suggestion ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Handle click on suggestion
                                    if(suggestion.full_address != null){
                                        onClick(suggestion.full_address)
                                        searchText = suggestion.full_address
                                        Log.d("ApiTesting", "Clicked on: ${suggestion.full_address}")
                                    } else if(suggestion.address != null){
                                        onClick(suggestion.address)
                                        searchText = suggestion.address
                                        Log.d("ApiTesting", "Clicked on: ${suggestion.address}")
                                    } else {
                                        onClick(suggestion.name)
                                        searchText = suggestion.name
                                        Log.d("ApiTesting", "Clicked on: ${suggestion.name}")
                                    }
                                    homelessViewModel.fetchSuggestions(
                                        query = "",
                                        sessionToken = sessionToken,
                                        proximity = "${currentLocation?.second},${currentLocation?.first}"
                                    )
                                }
                                .padding(16.dp)
                        ) {
                            Row {
                                Text(text = suggestion.name, style = MaterialTheme.typography.labelLarge)
                            }
                            Row {
                                if(suggestion.full_address != null){
                                    Text(
                                        text = suggestion.full_address.toString(),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = 0.6f
                                            )
                                        ),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
            is Resource.Error -> {
//                Text("Error: ${suggestedLocations.message}")
            }
        }
    }
}