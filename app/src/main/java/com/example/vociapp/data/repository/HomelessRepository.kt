package com.example.vociapp.data.repository

import androidx.work.await
import com.google.gson.Gson

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.SyncAction
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomelessRepository @Inject constructor(
    private val roomDataSource: RoomDataSource,
    private val firestoreDataSource: FirestoreDataSource,
    private val networkManager: NetworkManager,
    private val syncQueueDao: SyncQueueDao // Inject SyncQueueDao to manage offline sync
) {

    suspend fun addHomeless(homeless: Homeless): Resource<String> {
        return try {
            // Add to local data source
            roomDataSource.insertHomeless(homeless)

            // Check if the network is available
            if (networkManager.isNetworkConnected()) {
                // Sync with remote Firestore
                firestoreDataSource.addHomeless(homeless)
            } else {
                // Network unavailable, queue for later synchronization
                queueSyncAction("Homeless", "add", homeless)
                Resource.Error("Nessuna connessione di rete. Dati salvati localmente e messi in coda per la sincronizzazione")
            }
        } catch (e: Exception) {
            // Handle any unexpected errors
            Resource.Error("Errore sconosciuto: ${e.message}")
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
            syncQueueDao.getPendingSyncActions(System.currentTimeMillis()).collect{ pendingActions ->

                for (action in pendingActions) {
                    // Deserialize the data
                    //Log.d("SyncPendingActions", "Syncing action: $action")

                    if (action.entityType == "Homeless"){
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
        }
    }

    fun getHomelesses(): Flow<Resource<List<Homeless>>> = flow {
        emit(Resource.Loading()) // Emit loading state

        if (networkManager.isNetworkConnected()) {
            // If online, fetch from Firestore
            val remoteHomelesses = firestoreDataSource.getHomelesses()

            when (remoteHomelesses) {
                is Resource.Success -> {
                    // Cache the fetched data in Room using RoomDataSource
                    if (roomDataSource.syncQueueDao.isEmpty())
                        syncHomelessList(remoteHomelesses.data!!)
                    emit(remoteHomelesses) // Emit the remote data
                }
                is Resource.Error -> {
                    emit(remoteHomelesses) // Emit the error from Firestore
                }
                else -> {
                    emit(Resource.Loading()) // Handle any unexpected cases
                }
            }
        } else {
            // If offline, fetch from the local Room database
            roomDataSource.homelessDao.getAllHomeless().collect { localHomelesses ->
                emit(Resource.Success(localHomelesses)) // Emit the local data
            }
        }
    }

    suspend fun getHomeless(homelessID: String): Homeless? {
        return if (networkManager.isNetworkConnected()) {
            // If online, fetch from Firestore
            val remoteHomeless = firestoreDataSource.getHomeless(homelessID)
            if (remoteHomeless != null) {
                // Cache the fetched data into Room using RoomDataSource
                roomDataSource.homelessDao.insert(remoteHomeless)
            }
            remoteHomeless // Return the fetched data from Firestore
        } else {
            // If offline, fetch from the local Room database
            return roomDataSource.homelessDao.getHomelessById(homelessID)
        }
    }

    suspend fun updateHomeless(homeless: Homeless): Resource<Unit> {
        return firestoreDataSource.updateHomeless(homeless)
    }

    private suspend fun syncHomelessList(firestoreHomelessList: List<Homeless>) {
        try {
            val localHomelessList = roomDataSource.homelessDao.getAllHomeless().first() // Assuming getAllHomeless() returns a Flow

            for (firestoreHomeless in firestoreHomelessList) {
                val localHomeless = localHomelessList.find { it.id == firestoreHomeless.id }

                if (localHomeless != null) {
                    if (localHomeless != firestoreHomeless) {
                        roomDataSource.homelessDao.update(firestoreHomeless)
                    }
                } else {
                    roomDataSource.homelessDao.insert(firestoreHomeless)
                }
            }

            // Delete entries that exist locally but not in Firestore
            val homelessIdsToDelete = localHomelessList.map { it.id } - firestoreHomelessList.map { it.id }
                .toSet()
            for (homelessId in homelessIdsToDelete) {
                roomDataSource.homelessDao.deleteById(homelessId)
            }

            // Consider handling deletions if needed
        } catch (e: Exception) {
            // Handle error
        }
    }
}