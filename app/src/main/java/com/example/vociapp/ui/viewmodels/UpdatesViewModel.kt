package com.example.vociapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.repository.UpdatesRepository
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdatesViewModel @Inject constructor(
    private val updatesRepository: UpdatesRepository
) : ViewModel() {

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    private val _updates = MutableStateFlow<Resource<List<Update>>>(Resource.Loading())
    val updates: StateFlow<Resource<List<Update>>> = _updates.asStateFlow()

    init {
        fetchUpdates()
        getUpdates()
    }

    private fun getUpdates() {
        updatesRepository.getUpdates()
            .onEach { result ->
                _updates.value = result
            }
            .launchIn(viewModelScope)
    }

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

    fun fetchUpdates(){
        viewModelScope.launch {
            updatesRepository.fetchUpdatesFromFirestoreToRoom()
        }
    }
}