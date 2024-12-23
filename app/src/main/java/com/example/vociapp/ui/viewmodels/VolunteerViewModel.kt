package com.example.vociapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vociapp.data.repository.VolunteerRepository
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.util.Resource
import com.google.firebase.auth.FirebaseAuth
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

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _currentVolunteer = MutableStateFlow<Volunteer?>(null)

    private val _currentUser = MutableStateFlow<Volunteer?>(null) // Correct type
    val currentUser: StateFlow<Volunteer?> = _currentUser.asStateFlow()

    private val _userPreferencesResource = MutableStateFlow<Resource<List<String>>>(Resource.Loading())
    val userPreferencesResource: StateFlow<Resource<List<String>>> = _userPreferencesResource.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                Log.d("AuthStateListener", "User is logged in: ${firebaseUser.email}")
                viewModelScope.launch {
                    val volunteerId = volunteerRepository.getVolunteerIdByEmail(firebaseUser.email!!)
                    Log.d("AuthStateListener", "Volunteer ID: $volunteerId")
                    if (volunteerId != null){
                        _currentUser.value =
                            Volunteer(email = firebaseUser.email!!, id = volunteerId)
                        fetchUserPreferences(volunteerId)
                    }
                }
            } else {
                _currentUser.value = null // Update MutableStateFlow
            }
        }
    }

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

    fun fetchUserPreferences(userId: String) {
        volunteerRepository.getUserPreferences(userId)
            .onEach { result ->
                _userPreferencesResource.value = result
            }
            .launchIn(viewModelScope)
    }

    fun toggleHomelessPreference(userId: String, homelessId: String) {
        viewModelScope.launch {
            if (userPreferencesResource.value is Resource.Success) {
                val userPreferences = userPreferencesResource.value.data!!
                val updatedPreferredIds = if (homelessId in userPreferences) {
                    userPreferences - homelessId
                } else {
                    userPreferences + homelessId
                }
                viewModelScope.launch { // Launch a separate coroutine for updating preferences
                    val updateResult = volunteerRepository.updateUserPreferences(userId, updatedPreferredIds)
                    if (updateResult is Resource.Success) {
                        _userPreferencesResource.value.data = updatedPreferredIds // Emit new value to MutableStateFlow
                    } else if (updateResult is Resource.Error) {
                        // Handle error, e.g., show a Snackbar message
                        // You might want to revert the UI state here if the update fails
                    }
                }
            } else if (userPreferencesResource.value is Resource.Error) {
                // Handle error, e.g., show a Snackbar message
            }
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }
}