package com.example.vociapp.data.repository

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
            }
        } catch (e: Exception) {
            emit(Resource.Error("Errore durante il recupero dei dati: ${e.localizedMessage}"))
        }
    }

    suspend fun updateVolunteer(oldVolunteer: Volunteer, newVolunteer: Volunteer): Resource<Unit> {
        return firestoreDataSource.updateVolunteer(oldVolunteer, newVolunteer)
    }

    suspend fun completeVolunteerProfile(volunteer: Volunteer): Resource<Unit> {
        return firestoreDataSource.completeVolunteerProfile(volunteer)
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

}