package com.example.vociapp.data.repository

import android.util.Log
import androidx.lifecycle.get
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.UserPreferences
import com.example.vociapp.data.types.Volunteer
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.io.path.exists

class VolunteerRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) {

    suspend fun addVolunteer(volunteer: Volunteer): Resource<String> {
        return firestoreDataSource.addVolunteer(volunteer)
    }
    fun getVolunteerById(id: String): Flow<Resource<Volunteer>> = flow {
        emit(Resource.Loading())
        try {
            val volunteer = firestoreDataSource.getVolunteerById(id)
            if (volunteer != null) {
                emit(Resource.Success(volunteer))
            } else {
                emit(Resource.Error("Volontario non trovato"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Errore durante il recupero dei dati: ${e.localizedMessage}"))
        }
    }

    fun getVolunteerByNickname(nickname: String): Flow<Resource<Volunteer>> = flow {
        emit(Resource.Loading())
        try {
            val volunteer = firestoreDataSource.getVolunteerByNickname(nickname)
            if (volunteer != null) {
                emit(Resource.Success(volunteer))
            } else {
                emit(Resource.Error("Volontario non trovato"))
                Log.d("VolunteerRepo", "Volontario non trovato per il nickname: $nickname") // Log per vedere se entra qui
            }
        } catch (e: Exception) {
            emit(Resource.Error("Errore durante il recupero dei dati: ${e.localizedMessage}"))
            Log.e("VolunteerRepo", "Errore durante il recupero dei dati: ${e.localizedMessage}", e) // Log degli errori
        }
    }



    suspend fun updateVolunteer(volunteer: Volunteer): Resource<Unit> {
        return firestoreDataSource.updateVolunteer(volunteer)
    }

    fun getVolunteerByEmail(email: String): Flow<Resource<Volunteer>> = flow {
        emit(Resource.Loading())
        try {
            val volunteer = firestoreDataSource.getVolunteerByEmail(email)
            if (volunteer != null) {
                emit(Resource.Success(volunteer))
            } else {
                emit(Resource.Error("Volontario non trovato"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Errore durante il recupero dei dati: ${e.localizedMessage}"))
        }
    }

    suspend fun updateUserPreferences(userId: String, preferredHomelessIds: List<String>) {
        when (val result = firestoreDataSource.updateUserPreferences(userId, preferredHomelessIds)) {
            is Resource.Success -> {
                // Preference update successful
            }
            is Resource.Error -> throw Exception(result.message)
            else -> throw IllegalStateException("Unexpected Resource state")
        }
    }

    suspend fun getUserPreferences(userId: String): UserPreferences? {
        return when (val result = firestoreDataSource.getUserPreferences(userId)) {
            is Resource.Success -> result.data
            is Resource.Error -> throw Exception(result.message)
            else -> throw IllegalStateException("Unexpected Resource state")
        }
    }



}