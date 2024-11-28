package com.example.vociapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.repository.RequestRepositoryImpl
import com.example.vociapp.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val requestRepositoryImpl: RequestRepositoryImpl
) : ViewModel() {

    private val _requests = MutableStateFlow<Resource<List<Request>>>(Resource.Loading())
    val requests: StateFlow<Resource<List<Request>>> = _requests.asStateFlow()

    init {
        getRequests()
    }

    fun getRequests() {
        requestRepositoryImpl.getRequests()
            .onEach { result ->
                _requests.value = result
            }
            .launchIn(viewModelScope)
    }

    fun addRequest(request: Request) {
        viewModelScope.launch {
            // Handle the result of addRequest if needed
            val result = requestRepositoryImpl.addRequest(request)
            // ... (e.g., show a success message or handle errors)
        }
    }
}