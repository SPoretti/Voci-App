package com.example.vociapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.api.Suggestion
import com.example.vociapp.data.repository.MapboxRepository
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapboxViewModel(
    private val mapboxRepository: MapboxRepository
) : ViewModel() {
    //---------- State variables for updates ----------
    // Location Coordinates for Mapbox forward geocoding
    private val _locationCoordinates = MutableStateFlow<Resource<Pair<Double, Double>>>(Resource.Loading())
    val locationCoordinates: StateFlow<Resource<Pair<Double, Double>>> = _locationCoordinates
    // Location Address for Mapbox reverse geocoding
    private val _locationAddress = MutableStateFlow<Resource<String>>(Resource.Loading())
    val locationAddress: StateFlow<Resource<String>> = _locationAddress
    // Suggested Locations for Mapbox suggestions
    private val _suggestedLocations = MutableStateFlow<Resource<List<Suggestion>>>(Resource.Loading())
    val suggestedLocations: StateFlow<Resource<List<Suggestion>>> = _suggestedLocations

    fun fetchSuggestions(
        query: String,
        sessionToken: String,
        proximity: String? = null,
    ) {
        viewModelScope.launch {
            _suggestedLocations.value = Resource.Loading()
            try {
                val response = mapboxRepository.getMapboxSuggestions(
                    query = query,
                    accessToken = "pk.eyJ1IjoibXNib3JyYSIsImEiOiJjbTUxZzVkaDgxcHAzMmpzZXIycWgyM2hhIn0.kQRnLhjtCyT8l6LRI-B32g", // Replace with your token
                    sessionToken = sessionToken,
                    limit = 5,
                    language = "it",
                    country = "it",
                    proximity = proximity,
                    bbox = "9.0,45.3,9.3,45.6",
                    types = "place,neighborhood,street,address,poi"
                )
                _suggestedLocations.value = Resource.Success(response.suggestions)
            } catch (e: Exception) {
                Log.e("MapboxSearch", "Error: ${e.message}")
                _suggestedLocations.value = Resource.Error(e.message ?: "Errore durante la ricerca")
            }
        }
    }

    fun forwardGeocoding(query: String, proximity: String? = null) {
        viewModelScope.launch {
            _locationCoordinates.value = Resource.Loading()
            try {
                Log.d("ForwardGeocoding", query)
                val response = mapboxRepository.geocodeAddress(
                    query = query,
                    accessToken = "pk.eyJ1IjoibXNib3JyYSIsImEiOiJjbTUxZzVkaDgxcHAzMmpzZXIycWgyM2hhIn0.kQRnLhjtCyT8l6LRI-B32g",
                    language = "it",
                    country = "it",
                    types = "place,neighborhood,street,address",
                    bbox = "9.0,45.3,9.3,45.6",
                    proximity = proximity,
                )
                Log.d("ForwardGeocoding", response.toString())
                if (response.features.isNotEmpty()) {
                    val firstFeature = response.features[0]
                    val coordinates = firstFeature.geometry.coordinates
                    if (coordinates != null) { // Null check for coordinates
                        if (coordinates.size == 2) {
                            _locationCoordinates.value = Resource.Success(Pair(coordinates[1], coordinates[0]))
                            Log.d("ForwardGeocoding", coordinates.toString())
                        } else {
                            _locationCoordinates.value = Resource.Error("Coordinates are not just 2")
                            Log.d("ForwardGeocoding", "Coordinates are not just 2")
                        }
                    } else {
                        _locationCoordinates.value = Resource.Error("Coordinates are null")
                        Log.d("ForwardGeocoding", "Coordinates are null")
                    }
                } else {
                    _locationCoordinates.value = Resource.Error("Features Are null")
                    Log.d("ForwardGeocoding", "Features Are null")
                }
            } catch (e: Exception) {
                _locationCoordinates.value = Resource.Error(e.message ?: "Errore di geocoding")
                Log.d("ForwardGeocoding", e.message.toString())
            }
        }
    }

    fun reverseGeocoding(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _locationAddress.value = Resource.Loading()
            try {
                Log.d("ReverseGeocoding", "Reverse Geocoding: $latitude, $longitude")
                val response = mapboxRepository.reverseGeocode(
                    latitude = latitude.toString(),
                    longitude = longitude.toString(),
                    accessToken = "pk.eyJ1IjoibXNib3JyYSIsImEiOiJjbTUxZzVkaDgxcHAzMmpzZXIycWgyM2hhIn0.kQRnLhjtCyT8l6LRI-B32g",
                    language = "it",
                    types = "place,neighborhood,street,address"
                )
                Log.d("ReverseGeocoding", "Reverse Geocoding Response: $response")
                if (response.features.isNotEmpty()) {
                    val firstFeature = response.features[0]
                    val fullAddress = firstFeature.properties.full_address
                    val placeFormatted = firstFeature.properties.place_formatted
                    val placeName = firstFeature.place_name

                    val address = when {
                        !fullAddress.isNullOrBlank() -> fullAddress
                        !placeFormatted.isNullOrBlank() -> placeFormatted
                        !placeName.isNullOrBlank() -> placeName
                        else -> null
                    }
                    Log.d("ReverseGeocoding", "Address: $address")
                    if (address != null) {
                        _locationAddress.value = Resource.Success(address)
                    } else {
                        _locationAddress.value = Resource.Error("Indirizzo non trovato")
                    }
                    Log.d("ReverseGeocoding", "Address: $locationAddress")
                } else {
                    _locationAddress.value = Resource.Error("Indirizzo non trovato")
                }
            } catch (e: Exception) {
                _locationAddress.value = Resource.Error(e.message ?: "Errore di geocoding")
            }
        }
    }

    suspend fun forwardGeocodingAsync(query: String): Pair<Double, Double>? {
        Log.d("GeocodingAsync", "Starting geocoding for query: $query")
        return try {
            val response = mapboxRepository.geocodeAddress(
                query = query,
                accessToken = "pk.eyJ1IjoibXNib3JyYSIsImEiOiJjbTUxZzVkaDgxcHAzMmpzZXIycWgyM2hhIn0.kQRnLhjtCyT8l6LRI-B32g",
                language = "it",
                country = "it",
                types = "place,neighborhood,street,address"
            )
            Log.d("GeocodingAsync", "Geocoding response: $response")
            if (response.features.isNotEmpty()) {
                val firstFeature = response.features[0]
                val coordinates = firstFeature.geometry.coordinates
                if (coordinates != null) {
                    if (coordinates.size == 2) {
                        val result = Pair(coordinates[1], coordinates[0])
                        Log.d("GeocodingAsync", "Geocoding successful for query: $query, coordinates: $result")
                        result
                    } else {
                        Log.e("GeocodingAsync", "Coordinates size is not 2 for query: $query")
                        null
                    }
                } else {
                    Log.e("GeocodingAsync", "Coordinates are null for query: $query")
                    null
                }
            } else {
                Log.e("GeocodingAsync", "No features found for query: $query")
                null
            }
        } catch (e: Exception) {
            Log.e("GeocodingAsync", "Error during geocoding for query: $query, error: ${e.message}")
            null
        }
    }

    fun clearLocationVariables(){
        _locationCoordinates.value = Resource.Loading()
        _locationAddress.value = Resource.Loading()
    }
}