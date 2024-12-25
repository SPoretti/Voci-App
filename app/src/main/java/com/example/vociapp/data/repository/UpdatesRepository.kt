package com.example.vociapp.data.repository

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.dao.SyncQueueDao
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

class UpdatesRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val roomDataSource: RoomDataSource,
    private val networkManager: NetworkManager,
    private val syncQueueDao: SyncQueueDao // Inject SyncQueueDao to manage offline sync
) {

    suspend fun addUpdate(update: Update): Resource<String> {
        return try {
            // 1. Add to Room
            roomDataSource.updateDao.insert(update)

            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                firestoreDataSource.addUpdate(update)
            } else {
                // 3. If offline, queue for sync
                queueSyncAction("Update", "add", update)
                Resource.Error("No network connection. Data saved locally and queued for sync.")
            }
        } catch (e: Exception) {
            Resource.Error("Error adding update: ${e.message}")
        }
    }

    fun getUpdates(): Flow<Resource<List<Update>>> = flow {
        // 1. Emit cached data from Room immediately
        emit(Resource.Loading())
        roomDataSource.updateDao.getUpdates().collect { emit(Resource.Success(it)) }

        // 2. If online, fetch from Firestore and update Room
        if (networkManager.isNetworkConnected()) {
            try {
                val firestoreUpdates = firestoreDataSource.getUpdates().data!!
                syncUpdatesList(firestoreUpdates) // Update Room

                // 3. Emit updated data if it differs from cache
                val localUpdates = roomDataSource.updateDao.getUpdates().first()
                if (localUpdates != firestoreUpdates) {
                    emit(Resource.Success(localUpdates))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error syncing with Firestore: ${e.message}"))
            }
        }
    }

    suspend fun syncPendingActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            syncQueueDao.getPendingSyncActions(System.currentTimeMillis()).collect{ pendingActions ->

                for (action in pendingActions) {
                    // Deserialize the data
                    //Log.d("SyncPendingActions", "Syncing action: $action")

                    if (action.entityType == "Update"){
                        val data = Gson().fromJson(action.data, Update::class.java)
                        when (action.operation) {
                            "add" -> firestoreDataSource.addUpdate(data)
                            "update" -> firestoreDataSource.updateUpdate(data)
                            "delete" -> firestoreDataSource.deleteUpdate(data.id)
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

    private suspend fun syncUpdatesList(firestoreUpdatesList: List<Update>) {
        try {
            val localUpdatesList = roomDataSource.updateDao.getUpdates().first() // Assuming getUpdates() returns a Flow

            for (firestoreUpdate in firestoreUpdatesList) {
                val localUpdate = localUpdatesList.find { it.id == firestoreUpdate.id }

                if (localUpdate != null) {
                    if (localUpdate != firestoreUpdate) {
                        roomDataSource.updateDao.update(firestoreUpdate)
                    }
                } else {
                    roomDataSource.updateDao.insert(firestoreUpdate)
                }
            }

            // Delete entries that exist locally but not in Firestore
            val updateIdsToDelete = localUpdatesList.map { it.id } - firestoreUpdatesList.map { it.id }
                .toSet()
            for (updateId in updateIdsToDelete) {
                roomDataSource.updateDao.deleteById(updateId)
            }

            // Consider handling deletions if needed
        } catch (e: Exception) {
            // Handle error
        }
    }
}