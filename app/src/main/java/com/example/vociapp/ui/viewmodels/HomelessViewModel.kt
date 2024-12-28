package com.example.vociapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.repository.HomelessRepository
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.Job
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
) : ViewModel() {

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    private val _homelesses = MutableStateFlow<Resource<List<Homeless>>>(Resource.Loading())
    val homelesses: StateFlow<Resource<List<Homeless>>> = _homelesses.asStateFlow()

    private val _searchQuery = MutableStateFlow("") // Use MutableStateFlow
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredHomelesses = MutableStateFlow<Resource<List<Homeless>>>(Resource.Loading())
    val filteredHomelesses: StateFlow<Resource<List<Homeless>>> = _filteredHomelesses.asStateFlow()

    private val _homelessNames = MutableStateFlow<Map<String, String>>(emptyMap())
    val homelessNames: StateFlow<Map<String, String>> = _homelessNames.asStateFlow()

    init {
        fetchHomelesses()
        getHomelesses()
        fetchHomelessNames()
        updateSearchQuery("")
    }

    private var searchJob: Job? = null

    private fun fetchHomelesses() {
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

    suspend fun getHomeless(homelessID: String): Homeless? {
        return homelessRepository.getHomelessById(homelessID)
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
            val result = homelessRepository.updateHomeless(homeless)
            when(result) {
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

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

}