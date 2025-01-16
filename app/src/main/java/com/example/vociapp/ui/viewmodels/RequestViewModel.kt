package com.example.vociapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.RequestStatus
import com.example.vociapp.data.repository.RequestRepository
import com.example.vociapp.data.util.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class RequestViewModel @Inject constructor(
    private val requestRepository: RequestRepository    // Inject the RequestRepository
) : ViewModel() {

    //---------- State variables for requests ----------
    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage
    // Full list of requests
    private val _requests = MutableStateFlow<Resource<List<Request>>>(Resource.Loading())
    val requests: StateFlow<Resource<List<Request>>> = _requests.asStateFlow()
    // Active requests
    private val _activeRequests = MutableStateFlow<Resource<List<Request>>>(Resource.Loading())
    val activeRequests: StateFlow<Resource<List<Request>>> = _activeRequests.asStateFlow()
    // Completed requests
    private val _completedRequests = MutableStateFlow<Resource<List<Request>>>(Resource.Loading())
    val completedRequests: StateFlow<Resource<List<Request>>> = _completedRequests.asStateFlow()
    // Request by ID
    private val _requestById = MutableStateFlow<Resource<Request>>(Resource.Loading())
    val requestById: StateFlow<Resource<Request>> = _requestById.asStateFlow()
    // Requests by homeless ID
    private val _requestsByHomelessId = MutableStateFlow<Resource<List<Request>>>(Resource.Loading())
    val requestsByHomelessId: StateFlow<Resource<List<Request>>> = _requestsByHomelessId.asStateFlow()
    // Firebase Auth
    private val firebaseAuth = FirebaseAuth.getInstance()

    //---------- Firebase Auth Listener ----------
    init{
        firebaseAuth.addAuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                fetchRequests()
                getRequests()
            }
        }
    }

    //---------- Methods for requests ----------

    // Fetch requests from Firestore and update the local database
    fun fetchRequests() {
        viewModelScope.launch {
            requestRepository.fetchRequestsFromFirestoreToRoom()
        }
    }
    // Get requests from the local database
    fun getRequests() {
        requestRepository.getRequests()
            .onEach { result ->
                _requests.value = result
            }
            .launchIn(viewModelScope)
        getCompletedRequests()
        getActiveRequests()
    }
    // Get active requests from the local database
    fun getActiveRequests() {
        requestRepository.getActiveRequests()
            .onEach { result ->
                _activeRequests.value = result
            }
            .launchIn(viewModelScope)
    }
    // Get completed requests from the local database
    fun getCompletedRequests() {
        requestRepository.getCompletedRequests()
            .onEach { result ->
                _completedRequests.value = result
            }
            .launchIn(viewModelScope)
    }
    // Get a request by its ID from the local database
    fun getRequestById(requestId: String) {
        viewModelScope.launch {
            _requestById.value = Resource.Loading()
            val result = requestRepository.getRequestById(requestId)
            _requestById.value = result
        }
    }
    // Get requests by homeless ID from the local database
    fun getRequestsByHomelessId(homelessId: String) {
        requestRepository.getRequestsByHomelessId(homelessId)
            .onEach { result ->
                _requestsByHomelessId.value = result
            }
            .launchIn(viewModelScope)
    }
    // Add a new request to the local database
    fun addRequest(request: Request) {
        viewModelScope.launch {
            val result = requestRepository.addRequest(request)
            if (result is Resource.Success) {
                _snackbarMessage.value = "Richiesta aggiunta con successo!"
            } else if (result is Resource.Error) {
                _snackbarMessage.value = "Errore durante l'aggiunta della richiesta: ${result.message}"
            }
            getRequests()
        }
    }
    // Update an existing request in the local database
    fun updateRequest(request: Request) {
        viewModelScope.launch {
            when (val result = requestRepository.updateRequest(request)) {
                is Resource.Success -> {
                    getRequests()
                }
                is Resource.Error -> {
                    println("Error updating request: ${result.message}")
                }
                is Resource.Loading -> {}
            }
        }
        getRequests()
    }
    // Delete a request both locally and remotely
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
    // Update the status of a request to DONE
    fun requestDone(
        request: Request,
    ){
        request.status = RequestStatus.DONE
        updateRequest(request)
    }
    // Update the status of a request to to:do
    fun requestTodo(
        request: Request,
    ){
        request.status = RequestStatus.TODO
        updateRequest(request)
    }
    // Clear the snackbar message
    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }
}