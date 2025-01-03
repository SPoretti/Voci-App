package com.example.vociapp.data.repository

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.database.SyncAction
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdatesRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val roomDataSource: RoomDataSource,
    private val networkManager: NetworkManager,
) {

    suspend fun addUpdate(update: Update): Resource<Update> {
        return try {
            // 1. Add to Room
            roomDataSource.insertUpdate(update)

            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.addUpdate(update)
                if (resource is Resource.Success) {
                    Resource.Success(update)
                } else {
                    // 3. If firestore addition failed, queue for sync
                    queueSyncAction("Update", "add", update)
                    Resource.Error("Errore. \nDati salvati localmente in attesa di sincronizzazione.")
                }
            } else {
                // 4. If offline, queue for sync
                queueSyncAction("Update", "add", update)
                Resource.Error("Rete non disponibile.\nDati salvati localmente in attesa di sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

    //gets updates from room
    fun getUpdates(): Flow<Resource<List<Update>>> = flow {
        emit(Resource.Loading())
        roomDataSource.getUpdates().collect { emit(it) }
    }

    suspend fun syncPendingActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            roomDataSource.getPendingSyncActions(System.currentTimeMillis()).collect{ pendingActions ->

                for (action in pendingActions) {
                    if (action.entityType == "Update"){
                        // Deserialize the data
                        val data = Gson().fromJson(action.data, Update::class.java)
                        when (action.operation) {
                            "add" -> firestoreDataSource.addUpdate(data)
                            "update" -> firestoreDataSource.updateUpdate(data)
                            "delete" -> firestoreDataSource.deleteUpdate(data.id)
                        }
                        // Once synced, remove the action from the queue
                        roomDataSource.deleteSyncAction(action)
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
        roomDataSource.addSyncAction(syncAction)
    }

    suspend fun fetchUpdatesFromFirestoreToRoom(){
        if (networkManager.isNetworkConnected()){
            val firestoreUpdatesListResource = firestoreDataSource.getUpdates()

            if (firestoreUpdatesListResource is Resource.Success) {
                val firestoreUpdatesList = firestoreUpdatesListResource.data!!

                firestoreUpdatesList.forEach { remoteUpdate ->
                    roomDataSource.insertOrUpdateUpdate(remoteUpdate)
                }

                if (roomDataSource.isSyncQueueEmpty()) {
                    val localUpdatesList = roomDataSource.getUpdatesSnapshot()
                    //Delete entries that exist locally but not in Firestore
                    val updateIdsToDelete =
                        localUpdatesList.map { it.id }.minus(firestoreUpdatesList.map { it.id }
                            .toSet())
                    for (updateId in updateIdsToDelete) {
                        roomDataSource.deleteUpdateById(updateId)
                    }
                }
            }
        }
    }
}