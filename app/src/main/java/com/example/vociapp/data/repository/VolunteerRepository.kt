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

    fun getVolunteer(): Flow<Resource<List<Volunteer>>> = flow {
        emit(Resource.Loading()) // Indicate loading state
        val result = firestoreDataSource.getVolunteers() // Get the result from FirestoreDataSource
        emit(result) // Emit the result (Success or Error)
    }

    suspend fun updateVolunteer(volunteer: Volunteer): Resource<Unit> {
        return firestoreDataSource.updateVolunteer(volunteer)
    }
}