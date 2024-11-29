package com.example.vociapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.repository.HomelessRepository
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.util.Resource
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
    val homelesses: StateFlow<Resource<List<Homeless>>> = _homelesses.asStateFlow()

    init {
        getHomelesses()
    }

    fun getHomelesses() {
        homelessRepository.getHomelesses()
            .onEach { result ->
                _homelesses.value = result
            }
            .launchIn(viewModelScope)
    }

    fun addHomeless(homeless: Homeless) {
        viewModelScope.launch {
            // Handle the result of addHomeless if needed
            val result = homelessRepository.addHomeless(homeless)
            // ... (e.g., show a success message or handle errors)

            if (result is Resource.Success) {

                _snackbarMessage.value = "Senzatetto aggiunto con successo!"

            } else if (result is Resource.Error) {

                _snackbarMessage.value = "Errore durante l'aggiunta del senzatetto: ${result.message}"

            }

            // You might want to refresh the homelesses list after adding
            // getHomelesses()
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

}