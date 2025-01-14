package com.example.vociapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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

    private val _specificVolunteer = MutableStateFlow<Resource<Volunteer>>(Resource.Loading())
    val specificVolunteer: StateFlow<Resource<Volunteer>> = _specificVolunteer.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _currentUser = MutableStateFlow<Resource<Volunteer>>(Resource.Loading())
    val currentUser: StateFlow<Resource<Volunteer>> = _currentUser.asStateFlow()

    private var _userPreferencesResource = MutableStateFlow<Resource<List<String>>>(Resource.Loading())
    val userPreferencesResource: StateFlow<Resource<List<String>>> = _userPreferencesResource.asStateFlow()

    init {
        fetchVolunteers()

        firebaseAuth.addAuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                viewModelScope.launch {
                    val volunteer = firebaseUser.email?.let {
                        volunteerRepository.getVolunteerByEmail(it)
                    }
                    Log.d("AuthStateListener", "volunteer: ${volunteer?.data}")
                    if (volunteer?.data != null){
                        _currentUser.value = Resource.Success(
                            Volunteer(email = firebaseUser.email!!, id = volunteer.data?.id.toString()))
                        Log.d("AuthStateListener", "Volunteer ID: ${volunteer.data?.id}, ${volunteer.data?.email}")
                    } else {
                        Log.d("AuthStateListener", "Volunteer is null")
                    }
                    fetchVolunteers()
                }
            } else {
                _currentUser.value = Resource.Error("Utente non loggato")
            }
        }
    }

    //Ritorna il volontario connesso
    fun getCurrentUser(): Volunteer? {
        return _currentUser.value.data
    }

    fun getVolunteerById(volunteerId : String) {
        viewModelScope.launch {
            _specificVolunteer.value = Resource.Loading() // Set loading state
            val result = volunteerRepository.getVolunteerById(volunteerId) // Get data
            _specificVolunteer.value = result // Update state with result
        }
    }

    suspend fun checkIfNicknameExists(nickname: String): Boolean {
        val result = volunteerRepository.getVolunteerByNickname(nickname)
        return result is Resource.Success && result.data != null
    }

    suspend fun checkIfEmailExists(email: String): Boolean {
        return if(email.isEmpty()) false
        else volunteerRepository.getVolunteerByEmail(email).data != null
    }

    fun addVolunteer(volunteer: Volunteer, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = volunteerRepository.addVolunteer(volunteer)
            if (result is Resource.Success) {
                _snackbarMessage.value = "Registrazione effettuata"
                _currentUser.value = Resource.Success(volunteer)
                onComplete(true)
            } else if (result is Resource.Error) {
                _snackbarMessage.value = "Errore durante la registrazione: ${result.message}"
                onComplete(false)
            }
        }
    }


    fun updateVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            when (val result = volunteerRepository.updateVolunteer(volunteer)) {
                is Resource.Success -> {
                    getVolunteerById(volunteer.id)
                }
                is Resource.Error -> {
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

    fun fetchUserPreferences(email: String) {
        volunteerRepository.getUserPreferences(email)
            .onEach { result ->
                _userPreferencesResource.value = result
            }
            .launchIn(viewModelScope)
    }

    fun toggleHomelessPreference(userId: String, homelessId: String) {
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
}