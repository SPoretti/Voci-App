package com.example.vociapp.data.repository

import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomelessRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
){

    suspend fun addHomeless(homeless: Homeless): Resource<String> {
        return firestoreDataSource.addHomeless(homeless)
    }

    fun getHomelesses(): Flow<Resource<List<Homeless>>> = flow {
        emit(Resource.Loading()) // Indicate loading state
        val result = firestoreDataSource.getHomelesses() // Get the result from FirestoreDataSource
        emit(result) // Emit the result (Success or Error)

    }

    suspend fun getHomeless(homelessID: String): Homeless?{
        return firestoreDataSource.getHomeless(homelessID)
    }

    suspend fun updateHomeless(homeless: Homeless): Resource<Unit> {
        return firestoreDataSource.updateHomeless(homeless)
    }

}