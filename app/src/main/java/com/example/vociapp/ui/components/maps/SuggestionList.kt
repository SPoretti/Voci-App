package com.example.vociapp.ui.components.maps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.Point
import com.mapbox.search.SearchResultMetadata
import com.mapbox.search.common.RoutablePoint
import com.mapbox.search.record.HistoryRecord
import com.mapbox.search.result.SearchAddress
import com.mapbox.search.result.SearchResultType
import com.mapbox.search.ui.view.SearchResultAdapterItem

@Composable
fun SuggestionsList(suggestions: List<CarmenFeature>, onSuggestionClick: (CarmenFeature) -> Unit) {
    LazyColumn {
        items(suggestions) { suggestion ->
            val adapterItem = SearchResultAdapterItem.History(
                HistoryRecord(
                    suggestion.id() ?: "",
                    suggestion.placeName() ?: "",
                    descriptionText = suggestion.text() ?: "",
                    address = suggestion.address()?.let { SearchAddress(suggestion.address()) },
                    routablePoints = listOf(RoutablePoint(
                        suggestion.center() ?: Point.fromLngLat(0.0, 0.0), suggestion.placeName().toString()
                    )),
                    categories = (suggestion.properties()?.get("category")?.toString()?.split(",") ?: emptyList()).toList(),
                    makiIcon = suggestion.properties()?.get("maki")?.toString() ?: "",
                    coordinate = suggestion.center() ?: Point.fromLngLat(0.0, 0.0),
                    type = SearchResultType.valueOf(suggestion.placeType()?.firstOrNull() ?: "POI"),
                    metadata = null,
                    timestamp = System.currentTimeMillis()
                ),
                isFavorite = false
            )

            // Display the adapter item using a Text composable
            Text(
                text = adapterItem.record.address.toString(),
                modifier = Modifier.clickable { onSuggestionClick(suggestion) }
            )
        }
    }
}