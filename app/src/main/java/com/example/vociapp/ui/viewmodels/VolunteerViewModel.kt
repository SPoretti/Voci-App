package com.example.vociapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.repository.HomelessRepository
import com.example.vociapp.data.repository.VolunteerRepository
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.Volunteer
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class VolunteerViewModel @Inject constructor(
    private val volunteerRepository: VolunteerRepository
) : ViewModel() {

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    private val _volunteers = MutableStateFlow<Resource<List<Volunteer>>>(Resource.Loading())
    val volunteers: StateFlow<Resource<List<Volunteer>>> = _volunteers.asStateFlow()

    init {
        getVolunteers()
    }

    fun getVolunteers() {
        volunteerRepository.getVolunteer()
            .onEach { result ->
                _volunteers.value = result
            }
            .launchIn(viewModelScope)
    }

    fun addVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            // Handle the result of addVolunteer if needed
            val result = volunteerRepository.addVolunteer(volunteer)
            // ... (e.g., show a success message or handle errors)

            if (result is Resource.Success) {

                _snackbarMessage.value = "Registrazione effettuata"

            } else if (result is Resource.Error) {

                _snackbarMessage.value = "Errore durante la registrazione: ${result.message}"

            }

            // You might want to refresh the volunteers list after adding
            getVolunteers()
        }
    }

    fun updateVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            val result = volunteerRepository.updateVolunteer(volunteer)
            when (result) {
                is Resource.Success -> {
                    // Request updated successfully, you might want to refresh the requests list
                    getVolunteers()
                }
                is Resource.Error -> {
                    // Handle error, e.g., show an error message to the user
                    println("Errore nella modifica dell'utente: ${result.message}")
                }

                is Resource.Loading -> TODO()
            }
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

}