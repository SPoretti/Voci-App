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

    private val _specificVolunteer = MutableStateFlow<Resource<Volunteer?>>(Resource.Loading())
    val specificVolunteer: StateFlow<Resource<Volunteer?>> = _specificVolunteer.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()

    //current user logged in the app
    private val _currentUser = MutableStateFlow<Volunteer?>(null) // Correct type
    val currentUser: StateFlow<Volunteer?> = _currentUser.asStateFlow()

    init {
        fetchVolunteers()

        firebaseAuth.addAuthStateListener { auth ->
            auth.currentUser?.email?.let { email ->
                Log.d("AuthStateListener", "User is logged in: $email")
                viewModelScope.launch {
                    when (val volunteerIdResource = volunteerRepository.getVolunteerIdByEmail(email)) {
                        is Resource.Success -> {
                            val volunteerId = volunteerIdResource.data!!
                            val volunteer = volunteerRepository.getVolunteerById(volunteerId)
                            _currentUser.value =
                                if (volunteer is Resource.Success)
                                    volunteer.data
                                else
                                    null
                        }
                        is Resource.Error -> {
                            // Handle error fetching volunteer ID
                            Log.e("AuthStateListener", "Error fetching volunteer ID: ${volunteerIdResource.message}")
                        }
                        is Resource.Loading -> {
                            // Handle loading state if needed
                        }
                    }
                }
            } ?: run {
                _currentUser.value = null
            }
        }
    }

    fun setCurrentUser(volunteer: Volunteer) {
        _currentUser.value = volunteer
    }

    suspend fun setCurrentUserFromId(userId: String):Resource<Volunteer> {
        return try {
            val volunteer = volunteerRepository.getVolunteerById(userId)
            if (volunteer is Resource.Success) {
                _currentUser.value = volunteer.data
                Resource.Success(volunteer.data!!)
            }else
                Resource.Error("Errore durante il recupero dei dati dell'utente")
        } catch (e: Exception) {
            Resource.Error("Errore durante il recupero dei dati dell'utente: ${e.message}")
        }
    }

    fun getVolunteerById(id: String): StateFlow<Resource<Volunteer?>> {
        _specificVolunteer.value = Resource.Loading()
        viewModelScope.launch {
            _specificVolunteer.value = volunteerRepository.getVolunteerById(id)
        }
        return specificVolunteer
    }

    //Ritorna un volontario tramite nickname
    fun getVolunteerByNickname(nickname: String): StateFlow<Resource<Volunteer?>> {
        _specificVolunteer.value = Resource.Loading()
        viewModelScope.launch {
            _specificVolunteer.value = volunteerRepository.getVolunteerByNickname(nickname)
        }
        return specificVolunteer
    }

    fun getVolunteerByEmail(email: String): StateFlow<Resource<Volunteer?>>{
        _specificVolunteer.value = Resource.Loading()
        viewModelScope.launch {
            _specificVolunteer.value = volunteerRepository.getVolunteerByEmail(email)
        }
        return specificVolunteer
    }

    // ritorna il valore "name" del volontario corrente
    fun getVolunteerName(): String? {
        return _specificVolunteer.value.data?.name
    }

    // ritorna il valore "surname" del volontario corrente
    fun getVolunteerSurname(): String? {
        return _specificVolunteer.value.data?.surname
    }

    fun getCurrentVolunteer(): Volunteer? {
        return _specificVolunteer.value.data
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