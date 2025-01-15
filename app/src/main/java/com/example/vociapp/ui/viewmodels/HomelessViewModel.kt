package com.example.vociapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.repository.HomelessRepository
import com.example.vociapp.data.util.Resource
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
    private val homelessRepository: HomelessRepository,
    private val mapboxViewModel: MapboxViewModel
) : ViewModel() {
    //
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

    private val _locations = MutableStateFlow<Resource<List<Pair<Double, Double>>>>(Resource.Loading())
    val locations: StateFlow<Resource<List<Pair<Double, Double>>>> = _locations

    var coordinatesLoaded = false

    init {
        fetchHomelesses()
        getHomelesses()
        fetchHomelessNames()
        updateSearchQuery("")
    }

    private var searchJob: Job? = null

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

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
                is Resource.Loading -> {  }
            }
        }
        getHomelesses()
    }

    private fun fetchHomelessNames() {
        viewModelScope.launch {
            homelessRepository.getHomelesses()
                .collect { resource ->
                    if (resource is Resource.Success) {
                        val namesMap = resource.data?.associate { it.id to it.name }
                        if (namesMap != null)
                            _homelessNames.value = namesMap
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
                    mapboxViewModel.forwardGeocoding(homeless.data!!.location)
                _specificHomeless.value = homeless
            } catch (e: Exception) {
                _specificHomeless.value = Resource.Error(e.message ?: "Errore sconosciuto")
            }
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
                            val location = mapboxViewModel.forwardGeocodingAsync(homeless.location)
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
}