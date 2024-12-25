package com.example.vociapp.data.repository

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.SyncAction
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val roomDataSource: RoomDataSource,
    private val networkManager: NetworkManager,
    private val syncQueueDao: SyncQueueDao
) {

    suspend fun addRequest(request: Request): Resource<String> {
        return try {
            // 1. Add to Room
            roomDataSource.requestDao.insert(request)

            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                firestoreDataSource.addRequest(request)
            } else {
                // 3. If offline, queue for sync
                queueSyncAction("Request", "add", request)
                Resource.Error("No network connection. Data saved locally and queued for sync.")
            }
        } catch (e: Exception) {
            Resource.Error("Error adding request: ${e.message}")
        }
    }

    fun getRequests(): Flow<Resource<List<Request>>> = flow {
        // 1. Emit cached data from Room immediately
        emit(Resource.Loading())
        roomDataSource.requestDao.getAllRequests().collect { emit(Resource.Success(it)) }

        // 2. If online, fetch from Firestore and update Room
        if (networkManager.isNetworkConnected()) {
            try {
                val firestoreRequests = firestoreDataSource.getRequests().data!!
                syncRequestsList(firestoreRequests) // Update Room

                // 3. Emit updated data if it differs from cache
                val localRequests = roomDataSource.requestDao.getAllRequests().first()
//                if (localRequests != firestoreRequests) {
                emit(Resource.Success(localRequests))
                //}
            } catch (e: Exception) {
                emit(Resource.Error("Error syncing with Firestore: ${e.message}"))
            }
        }
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

    suspend fun syncPendingActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            syncQueueDao.getPendingSyncActions(System.currentTimeMillis()).collect{ pendingActions ->

                for (action in pendingActions) {
                    // Deserialize the data
                    //Log.d("SyncPendingActions", "Syncing action: $action")

                    if (action.entityType == "Request"){
                        val data = Gson().fromJson(action.data, Request::class.java)
                        when (action.operation) {
                            "add" -> firestoreDataSource.addRequest(data)
                            "update" -> firestoreDataSource.updateRequest(data)
                            "delete" -> firestoreDataSource.deleteRequest(data)
                        }
                        // Once synced, remove the action from the queue
                        syncQueueDao.deleteSyncAction(action)
                    }
                }
            }
        }
    }

    private suspend fun queueSyncAction(entityType: String, operation: String, data: Any) {
        // Serialize the data object to JSON
        val dataJson = Gson().toJson(data)

        // Create a new sync action to store in the queue
        val syncAction = SyncAction(
            entityType = entityType,
            operation = operation,
            data = dataJson,
            timestamp = System.currentTimeMillis()
        )

        // Add to the sync queue
        syncQueueDao.addSyncAction(syncAction)
    }

    private suspend fun syncRequestsList(firestoreRequestsList: List<Request>) {
        try {
            val localRequestsList = roomDataSource.requestDao.getAllRequests().first() // Assuming getRequests() returns a Flow

            for (firestoreRequest in firestoreRequestsList) {
                val localRequest = localRequestsList.find { it.id == firestoreRequest.id }

                if (localRequest != null) {
                    if (localRequest != firestoreRequest) {
                        roomDataSource.requestDao.update(firestoreRequest)
                    }
                } else {
                    roomDataSource.requestDao.insert(firestoreRequest)
                }
            }

            // Delete entries that exist locally but not in Firestore
            val requestIdsToDelete = localRequestsList.map { it.id } - firestoreRequestsList.map { it.id }
                .toSet()
            for (requestId in requestIdsToDelete) {
                roomDataSource.requestDao.deleteById(requestId)
            }

            // Consider handling deletions if needed
        } catch (e: Exception) {
            // Handle error
        }
    }
}