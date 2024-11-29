package com.example.vociapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.repository.RequestRepository
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class RequestViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _requests = MutableStateFlow<Resource<List<Request>>>(Resource.Loading())
    val requests: StateFlow<Resource<List<Request>>> = _requests.asStateFlow()

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

    fun addRequest(request: Request) {
        viewModelScope.launch {
            // Handle the result of addRequest if needed
            val result = requestRepository.addRequest(request)
            // ... (e.g., show a success message or handle errors)
            // You might want to refresh the requests list after adding
            // getRequests()


        }
    }
}