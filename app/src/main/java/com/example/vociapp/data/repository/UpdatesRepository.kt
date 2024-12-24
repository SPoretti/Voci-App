package com.example.vociapp.data.repository

import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.types.Update
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdatesRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) {

    suspend fun addUpdate(update: Update): Resource<String> {
        return firestoreDataSource.addUpdate(update)
    }

    fun getUpdates(): Flow<Resource<List<Update>>> = flow {
        emit(Resource.Loading()) // Indicate loading state
        val result = firestoreDataSource.getUpdates() // Get the result from FirestoreDataSource
        emit(result) // Emit the result (Success or Error)
    }
}