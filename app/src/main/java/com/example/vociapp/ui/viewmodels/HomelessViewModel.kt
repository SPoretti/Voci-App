package com.example.vociapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.remote.MapboxGeocodingClient
import com.example.vociapp.data.remote.MapboxSuggestionsClient
import com.example.vociapp.data.repository.HomelessRepository
import com.example.vociapp.data.util.Resource
import com.example.vociapp.data.util.Suggestion
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomelessViewModel @Inject constructor(
    private val homelessRepository: HomelessRepository
) : ViewModel() {

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    private val _homelesses = MutableStateFlow<Resource<List<Homeless>>>(Resource.Loading())
    val homelesses: StateFlow<Resource<List<Homeless>>> = _homelesses

    private val _specificHomeless = MutableStateFlow<Resource<Homeless>>(Resource.Loading())
    val specificHomeless: StateFlow<Resource<Homeless>> = _specificHomeless

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredHomelesses = MutableStateFlow<Resource<List<Homeless>>>(Resource.Loading())
    val filteredHomelesses: StateFlow<Resource<List<Homeless>>> = _filteredHomelesses.asStateFlow()

    private val _homelessNames = MutableStateFlow<Map<String, String>>(emptyMap())
    val homelessNames: StateFlow<Map<String, String>> = _homelessNames.asStateFlow()

    private val _locationCoordinates = MutableStateFlow<Resource<Pair<Double, Double>>>(Resource.Loading())
    val locationCoordinates: StateFlow<Resource<Pair<Double, Double>>> = _locationCoordinates

    private val _locationAddress = MutableStateFlow<Resource<String>>(Resource.Loading())
    val locationAddress: StateFlow<Resource<String>> = _locationAddress

    private val _locations = MutableStateFlow<Resource<List<Pair<Double, Double>>>>(Resource.Loading())
    val locations: StateFlow<Resource<List<Pair<Double, Double>>>> = _locations

    private val _suggestedLocations = MutableStateFlow<Resource<List<Suggestion>>>(Resource.Loading())
    val suggestedLocations: StateFlow<Resource<List<Suggestion>>> = _suggestedLocations

    var coordinatesLoaded = false

    init {
        fetchHomelesses()
        getHomelesses()
        fetchHomelessNames()
        updateSearchQuery("")
    }

    private var searchJob: Job? = null

    fun fetchHomelesses() {
        viewModelScope.launch {
            homelessRepository.fetchHomelessesFromFirestoreToRoom()
        }
    }

    fun updateSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            _searchQuery.value = query
            // Filter the list and update the filteredHomelessPeople state
            _homelesses.value.data?.let { homelessList ->
                val filteredList = filterHomelessPeople(query, homelessList)
                _filteredHomelesses.value = filteredList
            }
        }
    }

    private fun filterHomelessPeople(query: String, homelessList: List<Homeless>): Resource<List<Homeless>> {
        return if (homelessList.isEmpty()) {
            Resource.Error("Nessun risultato")
        } else {
            val filteredList = homelessList.filter { homeless ->
                homeless.name.contains(query, ignoreCase = true) or
                homeless.location.contains(query, ignoreCase = true)
            }
            Resource.Success(filteredList)
        }
    }

    fun getHomelesses() {
        homelessRepository.getHomelesses()
            .onEach { result ->
                _homelesses.value = result
            }
            .launchIn(viewModelScope)
    }

    suspend fun getHomelessById(homelessID: String): Homeless? {
        return try{
            val resource = homelessRepository.getHomelessById(homelessID)
            if (resource is Resource.Success) {
                resource.data
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("HomelessViewModel", "Error fetching homeless by ID: ${e.message}")
            null
        }
    }

    fun addHomeless(homeless: Homeless) {
        viewModelScope.launch {
            val result = homelessRepository.addHomeless(homeless)

            if (result is Resource.Success) {
                _snackbarMessage.value = "Senzatetto aggiunto con successo!"
            } else if (result is Resource.Error) {
                _snackbarMessage.value = "Errore durante l'aggiunta del senzatetto: ${result.message}"
            }

            getHomelesses()
        }
    }

    fun updateHomeless(homeless: Homeless){
        viewModelScope.launch {
            when(val result = homelessRepository.updateHomeless(homeless)) {
                is Resource.Success -> {
                    _snackbarMessage.value = "Senzatetto aggiornato con successo!"
                }

                is Resource.Error -> {
                    _snackbarMessage.value = "Errore durante l'aggiornamento del senzatetto: ${result.message}"
                }

                is Resource.Loading -> TODO()
            }
        }

        getHomelesses()
    }

    private fun fetchHomelessNames() {
        viewModelScope.launch {
            homelessRepository.getHomelesses()
                .collect { resource -> // Collect the flow
                    if (resource is Resource.Success) { // Check for success
                        val namesMap = resource.data?.associate { it.id to it.name }
                        if (namesMap != null)
                            _homelessNames.value = namesMap // Update state
                    } else if (resource is Resource.Error) {
                        Log.e("HomelessViewModel", "Error fetching homeless names: ${resource.message}")
                    }
                }
        }
    }

    fun getHomelessDetailsById(homelessId: String) {
        viewModelScope.launch {
            _specificHomeless.value = Resource.Loading()
            try {
                val homeless = homelessRepository.getHomelessById(homelessId)
                if (homeless is Resource.Success)
                    mapboxForwardGeocoding(homeless.data!!.location)
                _specificHomeless.value = homeless
            } catch (e: Exception) {
                _specificHomeless.value = Resource.Error(e.message ?: "Errore sconosciuto")
            }
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun fetchSuggestions(
        query: String,
        sessionToken: String,
        proximity: String? = null,
    ) {
        viewModelScope.launch {
            _suggestedLocations.value = Resource.Loading() // Set loading state
            try {
                val response = MapboxSuggestionsClient().getMapboxSuggestions(
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
                _suggestedLocations.value = Resource.Success(response.suggestions) // Set success state
            } catch (e: Exception) {
                Log.e("MapboxSearch", "Error: ${e.message}")
                _suggestedLocations.value = Resource.Error(e.message ?: "Errore durante la ricerca") // Set error state
            }
        }
    }

    fun mapboxForwardGeocoding(query: String, proximity: String? = null) {
        viewModelScope.launch {
            _locationCoordinates.value = Resource.Loading()
            try {
                Log.d("HomelessViewModel-mapboxForwardGeocoding", query)
                val response = MapboxGeocodingClient().geocodeAddress(
                    query = query,
                    accessToken = "pk.eyJ1IjoibXNib3JyYSIsImEiOiJjbTUxZzVkaDgxcHAzMmpzZXIycWgyM2hhIn0.kQRnLhjtCyT8l6LRI-B32g",
                    language = "it",
                    country = "it",
                    types = "place,neighborhood,street,address",
                    bbox = "9.0,45.3,9.3,45.6",
                    proximity = proximity,
                )
                Log.d("HomelessViewModel-mapboxForwardGeocoding", response.toString())
                if (response.features.isNotEmpty()) {
                    val firstFeature = response.features[0]
                    val coordinates = firstFeature.geometry.coordinates
                    if (coordinates != null) { // Null check for coordinates
                        if (coordinates.size == 2) {
                            _locationCoordinates.value = Resource.Success(Pair(coordinates[1], coordinates[0]))
                            Log.d("HomelessViewModel-mapboxForwardGeocoding", coordinates.toString())
                        } else {
                            _locationCoordinates.value = Resource.Error("Coordinates are not just 2")
                            Log.d("HomelessViewModel-mapboxForwardGeocoding", "Coordinates are not just 2")
                        }
                    } else {
                        _locationCoordinates.value = Resource.Error("Coordinates are null")
                        Log.d("HomelessViewModel-mapboxForwardGeocoding", "Coordinates are null")
                    }
                } else {
                    _locationCoordinates.value = Resource.Error("Features Are null")
                    Log.d("HomelessViewModel-mapboxForwardGeocoding", "Features Are null")
                }
            } catch (e: Exception) {
                _locationCoordinates.value = Resource.Error(e.message ?: "Errore di geocoding")
                Log.d("ApiTesting", e.message.toString())
            }
        }
    }

    fun mapboxReverseGeocoding(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _locationAddress.value = Resource.Loading()
            try {
                Log.d("ReverseGeocoding", "Reverse Geocoding: $latitude, $longitude")
                val response = MapboxGeocodingClient().reverseGeocode(
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

    // New async version of mapboxForwardGeocoding
    private suspend fun mapboxForwardGeocodingAsync(query: String): Pair<Double, Double>? {
        Log.d("GeocodingAsync", "Starting geocoding for query: $query")
        return try {
            val response = MapboxGeocodingClient().geocodeAddress(
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

    fun getAllCoordinates() {
        if (coordinatesLoaded) {
            Log.d("GetAllCoordinates", "Coordinates already loaded, skipping geocoding")
            return
        }
        viewModelScope.launch {
            _locations.value = Resource.Loading()
            Log.d("GetAllCoordinates", "Starting getAllCoordinates")
            try {
                val homelessList = homelesses.value.data ?: emptyList()
                Log.d("GetAllCoordinates", "Homeless list size: ${homelessList.size}")
                val coordinates = coroutineScope {
                    homelessList.map { homeless ->
                        async {
                            Log.d("GetAllCoordinates", "Geocoding location for homeless: ${homeless.name}, location: ${homeless.location}")
                            val location = mapboxForwardGeocodingAsync(homeless.location)
                            location
                        }
                    }.awaitAll().filterNotNull()
                }
                Log.d("GetAllCoordinates", "Geocoding completed, coordinates size: ${coordinates.size}")
                _locations.value = Resource.Success(coordinates)
                coordinatesLoaded = true
            } catch (e: Exception) {
                Log.e("GetAllCoordinates", "Error during getAllCoordinates: ${e.message}")
                _locations.value = Resource.Error(e.message ?: "Errore durante il geocoding")
            }
        }
    }

    fun clearLocationVariables(){
        _locationCoordinates.value = Resource.Loading()
        _locationAddress.value = Resource.Loading()
    }
}