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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        firebaseAuth.addAuthStateListener { auth ->
            Log.d("AuthStateListener", "Auth state changed")
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                Log.d("AuthStateListener", "Utente creato: ${firebaseUser.email}")
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
                Log.d("Inserimento Volontario", "Volontario ${_currentUser.value.data}")
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
}