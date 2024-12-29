package com.example.vociapp.ui.components.maps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.Point
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.search.ui.view.CommonSearchViewConfiguration
import com.mapbox.search.ui.view.DistanceUnitType
import com.mapbox.search.ui.view.SearchResultsView

@Composable
fun MapboxSearchComponent() {
    var searchText by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf<List<CarmenFeature>>(emptyList()) }
    var selectedSuggestion by remember { mutableStateOf<CarmenFeature?>(null) }
    val context = LocalContext.current
    val mapViewportState = rememberMapViewportState()
    var searchResultsView by remember { mutableStateOf<SearchResultsView?>(null) }

    searchResultsView?.initialize(
        SearchResultsView.Configuration(
            CommonSearchViewConfiguration(DistanceUnitType.METRIC)
        )
    )

    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
            },
            label = { Text("Search for a place...") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        )

        SuggestionsList(suggestions) { suggestion ->
            selectedSuggestion = suggestion
        }

        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState
        ) {
            // ... add markers and other map elements ...
        }
    }

//    LaunchedEffect(suggestions) {
//        searchResultsView? = object : SearchCallback {
//            override fun onResults(results: List<CarmenFeature>) {
//                suggestions = results
//            }
//
//            override fun onError(e: Exception) {
//                // Handle error
//            }
//        }
//    }

    LaunchedEffect(selectedSuggestion) {
        selectedSuggestion?.let { carmenFeature ->
            val coordinates = carmenFeature.center()
            mapViewportState.flyTo(
                cameraOptions {
                    center(Point.fromLngLat(coordinates?.latitude() ?: 9.19, coordinates?.longitude() ?: 45.4642))
                    zoom(15.0)
                }
            )
        }
    }
}
