package com.example.vociapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.repository.VolunteerRepository
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

    private val _volunteers = MutableStateFlow<Resource<Volunteer>>(Resource.Loading())

    private val _specificVolunteer = MutableStateFlow<Resource<Volunteer>>(Resource.Loading())
    val specificVolunteer: StateFlow<Resource<Volunteer>> = _specificVolunteer.asStateFlow()

    private val _currentVolunteer = MutableStateFlow<Volunteer?>(null)

    init {
        getVolunteerById(id = "")
    }

    private fun getVolunteerById(id: String) {
        volunteerRepository.getVolunteerById(id)
            .onEach { result ->
                _volunteers.value = result
                if (result is Resource.Success && result.data != null) {
                    _currentVolunteer.value = result.data
                }
            }
            .launchIn(viewModelScope)
    }

    //Ritorna un volontario tramite nickname
    fun getVolunteerByNickname(nickname: String): StateFlow<Resource<Volunteer>> {
        _specificVolunteer.value = Resource.Loading()
        viewModelScope.launch {
            volunteerRepository.getVolunteerByNickname(nickname)
                .collect { result ->
                    _specificVolunteer.value = result
                }
        }
        return _specificVolunteer.asStateFlow()
    }

    //Ritorna un volontario tramite email
    fun getVolunteerByEmail(email: String?): StateFlow<Resource<Volunteer>> {
        _specificVolunteer.value = Resource.Loading()
        viewModelScope.launch {
            if (email != null) {
                volunteerRepository.getVolunteerByEmail(email)
                    .collect { result ->
                        _specificVolunteer.value = result
                    }
            }
        }
        return _specificVolunteer.asStateFlow()
    }

    fun addVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            val result = volunteerRepository.addVolunteer(volunteer)
            if (result is Resource.Success) {
                _snackbarMessage.value = "Registrazione effettuata"
                _currentVolunteer.value = volunteer
            } else if (result is Resource.Error) {
                _snackbarMessage.value = "Errore durante la registrazione: ${result.message}"
            }
        }
    }


    fun updateVolunteer(oldVolunteer: Volunteer, newVolunteer: Volunteer) {
        viewModelScope.launch {
            when (val result = volunteerRepository.updateVolunteer(oldVolunteer, newVolunteer)) {
                is Resource.Success -> {
                    getVolunteerById(newVolunteer.id)
                }
                is Resource.Error -> {
                    println("Errore nella modifica dell'utente: ${result.message}")
                }
                is Resource.Loading -> TODO()
            }
        }
    }

}