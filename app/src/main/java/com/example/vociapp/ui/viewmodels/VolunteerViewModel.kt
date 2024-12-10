package com.example.vociapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.repository.HomelessRepository
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
    private val volunteerRepository: VolunteerRepository,
) : ViewModel() {

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    private val _volunteers = MutableStateFlow<Resource<Volunteer>>(Resource.Loading())

    private val _specificVolunteer = MutableStateFlow<Resource<Volunteer>>(Resource.Loading())
    val specificVolunteer: StateFlow<Resource<Volunteer>> = _specificVolunteer.asStateFlow()

    private val _currentVolunteer = MutableStateFlow<Volunteer?>(null)

    init {
        getVolunteerById(id = "")
    }

    fun getVolunteerById(id: String) {
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

    fun getVolunteerByEmail(email: String): StateFlow<Resource<Volunteer>> {
        _specificVolunteer.value = Resource.Loading()
        viewModelScope.launch {
            volunteerRepository.getVolunteerByEmail(email)
                .collect { result ->
                    _specificVolunteer.value = result
                }
        }
        return _specificVolunteer.asStateFlow()
    }

    // ritorna il valore "name" del volontario corrente
    fun getVolunteerName(): String? {
        return _currentVolunteer.value?.name
    }

    // ritorna il valore "surname" del volontario corrente
    fun getVolunteerSurname(): String? {
        return _currentVolunteer.value?.surname
    }

    fun getCurrentVolunteer(): Volunteer? {
        return _currentVolunteer.value
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


    fun updateVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            val result = volunteerRepository.updateVolunteer(volunteer)
            when (result) {
                is Resource.Success -> {
                    // Request updated successfully, you might want to refresh the requests list
                    getVolunteerById(volunteer.id)
                }
                is Resource.Error -> {
                    // Handle error, e.g., show an error message to the user
                    println("Errore nella modifica dell'utente: ${result.message}")
                }

                is Resource.Loading -> TODO()
            }
        }
    }



    suspend fun isPreferred(userId: String, homelessId: String): Boolean {
        val preferredHomelessIds = volunteerRepository.getUserPreferences(userId = userId)
        if (preferredHomelessIds != null) {
            return preferredHomelessIds.preferredHomelessIds.contains(homelessId) ?: false
        }
        return false

    }

    fun toggleHomelessPreference(userId: String, homelessId: String) {
        viewModelScope.launch {
            val userPreferences = volunteerRepository.getUserPreferences(userId)
            val updatedPreferredIds = if (userPreferences?.preferredHomelessIds?.contains(homelessId) == true) {
                userPreferences?.preferredHomelessIds?.minus(homelessId)
            } else {
                userPreferences?.preferredHomelessIds?.plus(homelessId)
            }
            if (updatedPreferredIds != null) {
                volunteerRepository.updateUserPreferences(userId, updatedPreferredIds)
            }
            // Update the UI state (e.g., using a StateFlow) to reflect the preference change
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

}