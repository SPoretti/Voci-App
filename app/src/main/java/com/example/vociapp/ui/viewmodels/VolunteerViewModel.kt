package com.example.vociapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.repository.VolunteerRepository
import com.example.vociapp.data.util.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class VolunteerViewModel @Inject constructor(
    private val volunteerRepository: VolunteerRepository,
) : ViewModel() {

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    private val _volunteers = MutableStateFlow<Resource<Volunteer>>(Resource.Loading())
    val volunteers: StateFlow<Resource<Volunteer>> = _volunteers.asStateFlow()

    private val _specificVolunteer = MutableStateFlow<Resource<Volunteer>>(Resource.Loading())
    val specificVolunteer: StateFlow<Resource<Volunteer>> = _specificVolunteer.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _currentUser = MutableStateFlow<Volunteer?>(null) // Correct type
    val currentUser: StateFlow<Volunteer?> = _currentUser.asStateFlow()

    init {
        fetchVolunteers()

        firebaseAuth.addAuthStateListener { auth ->
            auth.currentUser?.email?.let { email ->
                Log.d("AuthStateListener", "User is logged in: $email")
                viewModelScope.launch {
                    try {
                        val volunteerId = volunteerRepository.getVolunteerIdByEmail(email)
                        volunteerId?.let {
                            _currentUser.value = volunteerRepository.getVolunteerById(it)
                        } ?: run {
                            // Handle case where volunteerId is null
                        }
                    } catch (e: Exception) {
                        // Handle error
                    }
                }
            } ?: run {
                _currentUser.value = null
            }
        }
    }

    suspend fun getVolunteerById(id: String) {
        _currentUser.value = volunteerRepository.getVolunteerById(id)
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
        return _currentUser.value?.name
    }

    // ritorna il valore "surname" del volontario corrente
    fun getVolunteerSurname(): String? {
        return _currentUser.value?.surname
    }

    fun getCurrentVolunteer(): Volunteer? {
        return _currentUser.value
    }

    fun addVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            val result = volunteerRepository.addVolunteer(volunteer)
            if (result is Resource.Success) {
                _snackbarMessage.value = "Registrazione effettuata"
                _currentUser.value = volunteer
            } else if (result is Resource.Error) {
                _snackbarMessage.value = "Errore durante la registrazione: ${result.message}"
            }
        }
    }

    fun updateVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            val result = volunteerRepository.updateVolunteer(volunteer)
            when (result){
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

    fun fetchVolunteers() {
        viewModelScope.launch {
            volunteerRepository.fetchVolunteersFromFirestoreToRoom()
        }
    }

    fun toggleHomelessPreference(homelessId: String) {
        viewModelScope.launch {
            val currentVolunteer = currentUser.value
            if (currentVolunteer != null) {
                val updatedPreferredIds =
                    if (homelessId in currentVolunteer.preferredHomelessIds) {
                        currentVolunteer.preferredHomelessIds - homelessId
                    } else {
                        currentVolunteer.preferredHomelessIds + homelessId
                    }
                volunteerRepository.updateVolunteer(currentVolunteer.copy(preferredHomelessIds = updatedPreferredIds))
                // Update _currentUser state to reflect the change
                _currentUser.value = currentVolunteer.copy(preferredHomelessIds = updatedPreferredIds)
            }
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }
}