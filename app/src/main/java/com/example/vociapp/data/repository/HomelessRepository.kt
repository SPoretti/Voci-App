package com.example.vociapp.data.repository

import com.google.gson.Gson

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.SyncAction
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomelessRepository @Inject constructor(
    private val localDataSource: RoomDataSource,
    private val firestoreDataSource: FirestoreDataSource,
    private val networkManager: NetworkManager,
    private val syncQueueDao: SyncQueueDao // Inject SyncQueueDao to manage offline sync
) {

    suspend fun addHomeless(homeless: Homeless): Resource<String> {
        // Add to local data source
        localDataSource.insertHomeless(homeless)

        // Check if the network is available
        return if (networkManager.isNetworkConnected()) {
            // Sync with remote Firestore immediately
            firestoreDataSource.addHomeless(homeless)
        } else {
            // Network is unavailable, queue for later synchronization
            queueSyncAction("Homeless", "add", homeless)
            Resource.Error("No network connection. Data saved locally and queued for later sync.")
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

    // Method to sync pending actions when the device is online
    suspend fun syncPendingActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            val pendingActions = syncQueueDao.getPendingSyncActions(System.currentTimeMillis())

            for (action in pendingActions) {
                // Deserialize the data
                val data = Gson().fromJson(action.data, Homeless::class.java)

                when (action.operation) {
                    "add" -> firestoreDataSource.addHomeless(data)
                    "update" -> firestoreDataSource.updateHomeless(data)
                    "delete" -> firestoreDataSource.deleteHomeless(data.id)
                }

                // Once synced, remove the action from the queue
                syncQueueDao.deleteSyncAction(action)
            }
        }
    }
    fun getHomelesses(): Flow<Resource<List<Homeless>>> = flow {
        emit(Resource.Loading()) // Indicate loading state
        val result = firestoreDataSource.getHomelesses() // Get the result from FirestoreDataSource
        emit(result) // Emit the result (Success or Error)

    }

    suspend fun getHomeless(homelessID: String): Homeless?{
        return firestoreDataSource.getHomeless(homelessID)
    }
}




