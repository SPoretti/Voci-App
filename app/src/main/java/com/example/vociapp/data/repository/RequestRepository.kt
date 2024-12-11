package com.example.vociapp.data.repository

import com.example.vociapp.data.types.Request
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) {

    suspend fun addRequest(request: Request): Resource<String> {
        return firestoreDataSource.addRequest(request)
    }

    fun getRequests(): Flow<Resource<List<Request>>> = flow {
        emit(Resource.Loading()) // Indicate loading state
        val result = firestoreDataSource.getRequests() // Get the result from FirestoreDataSource
        emit(result) // Emit the result (Success or Error)
    }

    suspend fun updateRequest(request: Request): Resource<Unit> {
        return firestoreDataSource.updateRequest(request)
    }

    suspend fun deleteRequest(request: Request): Resource<Unit> {
        return firestoreDataSource.deleteRequest(request)
    }

    suspend fun getRequestById(requestId: String): Resource<Request> {
        return firestoreDataSource.getRequestById(requestId)
    }
}