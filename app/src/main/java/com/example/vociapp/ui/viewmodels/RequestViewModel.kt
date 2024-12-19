package com.example.vociapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.repository.RequestRepository
import com.example.vociapp.data.types.RequestStatus
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class RequestViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    private val _requests = MutableStateFlow<Resource<List<Request>>>(Resource.Loading())
    val requests: StateFlow<Resource<List<Request>>> = _requests.asStateFlow()

    private val _requestById = MutableStateFlow<Resource<Request>>(Resource.Loading())
    val requestById: StateFlow<Resource<Request>> = _requestById.asStateFlow()

    init {
        getRequests()
    }

    fun getRequests() {
        requestRepository.getRequests()
            .onEach { result ->
                _requests.value = result
            }
            .launchIn(viewModelScope)
    }

    fun getRequestById(requestId: String) {
        viewModelScope.launch {
            _requestById.value = Resource.Loading() // Set loading state
            val result = requestRepository.getRequestById(requestId) // Get data
            _requestById.value = result // Update state with result
        }
    }

    fun addRequest(request: Request) {
        viewModelScope.launch {
            // Handle the result of addRequest if needed
            val result = requestRepository.addRequest(request)

            if (result is Resource.Success) {

                _snackbarMessage.value = "Richiesta aggiunta con successo!"

            } else if (result is Resource.Error) {

                _snackbarMessage.value = "Errore durante l'aggiunta della richiesta: ${result.message}"

            }

            // ... (e.g., show a success message or handle errors)
            // You might want to refresh the requests list after adding
            getRequests()


        }
    }

    fun updateRequest(request: Request) {
        viewModelScope.launch {
            val result = requestRepository.updateRequest(request)
            when (result) {
                is Resource.Success -> {
                    // Request updated successfully, you might want to refresh the requests list
                    getRequests()
                }
                is Resource.Error -> {
                    // Handle error, e.g., show an error message to the user
                    println("Error updating request: ${result.message}")
                }

                is Resource.Loading -> TODO()
            }
        }
    }

    fun requestDone(
        request: Request,
    ){
        request.status = RequestStatus.DONE
        updateRequest(request)
    }

    fun deleteRequest(request: Request) {
        viewModelScope.launch {
            val result = requestRepository.deleteRequest(request)

            if (result is Resource.Success) {
                _snackbarMessage.value = "Richiesta eliminata con successo!"
            } else if (result is Resource.Error) {
                _snackbarMessage.value = "Errore durante l'eliminazione della richiesta: ${result.message}"
            }

            // Refresh the requests list
            getRequests()
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }
}