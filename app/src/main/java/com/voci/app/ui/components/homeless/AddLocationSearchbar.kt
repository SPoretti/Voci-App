package com.voci.app.ui.components.homeless

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.voci.app.data.util.Resource
import com.voci.app.di.LocalServiceLocator
import com.voci.app.ui.components.core.LocationHandler
import java.util.UUID

@Composable
fun AddLocationSearchbar (
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val serviceLocator = LocalServiceLocator.current
    val mapboxViewModel = serviceLocator.obtainMapboxViewModel()
    val sessionToken = UUID.randomUUID().toString()

    var searchText by remember { mutableStateOf("") }
    val suggestedLocations by mapboxViewModel.suggestedLocations.collectAsState()

    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationHandler = remember {
        LocationHandler(context, fusedLocationClient)
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
                mapboxViewModel.fetchSuggestions(
                    query = newText,
                    sessionToken = sessionToken,
                    proximity = "${currentLocation?.second},${currentLocation?.first}"
                )
                Log.d("AddLocationSearchbar", newText)
            },
            placeholder = {
                Text(
                    text = "Cerca...",
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = Color.Transparent,
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
                                mapboxViewModel.fetchSuggestions(
                                    query = searchText,
                                    sessionToken = sessionToken,
                                    proximity = "${currentLocation?.second},${currentLocation?.first}"
                                )
                                Log.d("AddLocationSearchbar", searchText)
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
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
        )
        when (suggestedLocations) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                val sortedSuggestions = suggestedLocations.data.orEmpty()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.background)
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
                                        Log.d("AddLocationSearchbar", "Clicked on: ${suggestion.full_address}")
                                    } else if(suggestion.address != null){
                                        onClick(suggestion.address)
                                        searchText = suggestion.address
                                        Log.d("AddLocationSearchbar", "Clicked on: ${suggestion.address}")
                                    } else {
                                        onClick(suggestion.name)
                                        searchText = suggestion.name
                                        Log.d("AddLocationSearchbar", "Clicked on: ${suggestion.name}")
                                    }
                                    mapboxViewModel.fetchSuggestions(
                                        query = "",
                                        sessionToken = sessionToken,
                                        proximity = "${currentLocation?.second},${currentLocation?.first}"
                                    )
                                }
                                .padding(horizontal = 16.dp)
                                .height(56.dp),
                            verticalArrangement = Arrangement.Center
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
//                                    Spacer(modifier = Modifier.height(8.dp))
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