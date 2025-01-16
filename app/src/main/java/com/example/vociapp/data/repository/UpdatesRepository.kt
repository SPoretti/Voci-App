package com.example.vociapp.data.repository

import android.util.Log
import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdatesRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,   // Remote data source
    private val roomDataSource: RoomDataSource,             // Local data source
    private val networkManager: NetworkManager,             // Network manager for online/offline check
) {
    // Add new Update (both locally and remotely)
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
                    roomDataSource.addSyncAction("Update", "add", update)
                    Resource.Error("Errore. \nDati salvati localmente in attesa di sincronizzazione.")
                }
            } else {
                // 4. If offline, queue for sync
                roomDataSource.addSyncAction("Update", "add", update)
                Resource.Error("Rete non disponibile.\nDati salvati localmente in attesa di sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

    //gets updates from room and wrap the result in a resource
    fun getUpdates(): Flow<Resource<List<Update>>> = flow {
        try {
            emit(Resource.Loading()) // Indicate loading state
            roomDataSource.getUpdates().collect { updates ->
                emit(Resource.Success(updates)) // Emit success with the fetched list
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching updates: ${e.message}")) // Emit error if there's an issue
        }
    }

    fun getUpdatesByHomelessId(homelessId: String): Flow<Resource<List<Update>>> = flow {
        try {
            emit(Resource.Loading()) // Indicate loading state
            roomDataSource.getUpdatesByHomelessId(homelessId).collect { updates ->
                emit(Resource.Success(updates)) // Emit success with the fetched list
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching updates: ${e.message}")) // Emit error if there's an issue
        }
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

                        var result : Resource<*> = Resource.Loading<Unit>()

                        when (action.operation) {
                            "add" -> result = firestoreDataSource.addUpdate(data)
                            //"update" -> result = firestoreDataSource.updateUpdate(data)
                            //"delete" -> result = firestoreDataSource.deleteUpdateById(data.id)
                            else -> {
                                Log.d(
                                    "SyncPendingActions",
                                    "Unknown operation: ${action.operation}"
                                )
                            }
                        }
                        if (result is Resource.Success){
                            // Once synced, remove the action from the queue
                            roomDataSource.deleteSyncAction(action)
                        }
                    }
                }
            }
        }
    }

    // Update the local database with the latest data from Firestore
    suspend fun fetchUpdatesFromFirestoreToRoom(){
        // Only attempt to fetch if the device is online
        if (networkManager.isNetworkConnected()){
            val firestoreUpdatesListResource = firestoreDataSource.getUpdates()

            if (firestoreUpdatesListResource is Resource.Success) {
                val firestoreUpdatesList = firestoreUpdatesListResource.data!!

                firestoreUpdatesList.forEach { remoteUpdate ->
                    roomDataSource.insertOrUpdateUpdate(remoteUpdate)
                }
                // Only delete if the syncronization from room to firestore has already happened
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