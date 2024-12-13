package com.example.vociapp.data.repository

import android.util.Log
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.types.Volunteer
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

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

    suspend fun getVolunteerIdByEmail(email: String): String? {
        return firestoreDataSource.getVolunteerIdByEmail(email)
    }

    suspend fun updateUserPreferences(userId: String, preferredHomelessIds: List<String>): Resource<Unit> {
        return firestoreDataSource.updateUserPreferences(userId, preferredHomelessIds)

    }

    fun getUserPreferences(userId: String): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading()) // Indicate loading state
        val result = firestoreDataSource.getUserPreferences(userId) // Get the result from FirestoreDataSource
        emit(result) // Emit the result (Success or Error)
    }



}