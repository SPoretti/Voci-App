package com.voci.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voci.app.data.local.database.Update
import com.voci.app.data.repository.UpdatesRepository
import com.voci.app.data.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdatesViewModel @Inject constructor(
    private val updatesRepository: UpdatesRepository    // Inject the UpdatesRepository
) : ViewModel() {
    //---------- State variables for updates ----------
    // Snackbar Message
    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage
    // Full list of updates
    private val _updates = MutableStateFlow<Resource<List<Update>>>(Resource.Loading())
    val updates: StateFlow<Resource<List<Update>>> = _updates.asStateFlow()
    // Updates by homeless ID
    private val _updatesByHomelessId = MutableStateFlow<Resource<List<Update>>>(Resource.Loading())
    val updatesByHomelessId: StateFlow<Resource<List<Update>>> = _updatesByHomelessId.asStateFlow()
    // Initialize the ViewModel
    init {
        fetchUpdates()
        getUpdates()
    }
    // Fetch updates from Firestore and update the local database
    fun fetchUpdates(){
        viewModelScope.launch {
            updatesRepository.fetchUpdatesFromFirestoreToRoom()
        }
    }
    // Get updates from the repository
    private fun getUpdates() {
        updatesRepository.getUpdates()
            .onEach { result ->
                _updates.value = result
            }
            .launchIn(viewModelScope)
    }
    // Get updates by homeless ID from the repository
    fun getUpdatesByHomelessId(homelessId: String) {
        updatesRepository.getUpdatesByHomelessId(homelessId)
            .onEach { result ->
                _updatesByHomelessId.value = result
            }
            .launchIn(viewModelScope)
    }
    // Add a new update to the repository
    fun addUpdate(update: Update) {
        viewModelScope.launch {
            val result = updatesRepository.addUpdate(update)
            if (result is Resource.Success) {
                _snackbarMessage.value = "Aggiornamento aggiunto con successo!"
            } else if (result is Resource.Error) {
                _snackbarMessage.value = "Errore durante l'aggiunta dell'aggiornamento: ${result.message}"
            }
            getUpdates()
        }
    }
}