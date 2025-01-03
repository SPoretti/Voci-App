package com.example.vociapp.data.repository

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.SyncAction
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomelessRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val roomDataSource: RoomDataSource,
    private val networkManager: NetworkManager,
) {

    suspend fun addHomeless(homeless: Homeless): Resource<Homeless> {
        return try {
            // 1. Add to Room
            roomDataSource.insertHomeless(homeless)

            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.addHomeless(homeless)
                if (resource is Resource.Success) {
                    Resource.Success(homeless)
                } else {
                    // 3. If firestore addition failed, queue for sync
                    queueSyncAction("Homeless", "add", homeless)
                    Resource.Error("Errore. \nDati salvati localmente in attesa di sincronizzazione.")
                }
            } else {
                // 4. If offline, queue for sync
                queueSyncAction("Homeless", "add", homeless)
                Resource.Error("Rete non disponibile.\nDati salvati localmente in attesa di sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
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

    // Method to sync pending actions when the device is online
    suspend fun syncPendingActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            roomDataSource.getPendingSyncActions(System.currentTimeMillis()).collect{ pendingActions ->

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
                        roomDataSource.deleteSyncAction(action)
                    }
                }
            }
        }
    }

    fun getHomelesses(): Flow<Resource<List<Homeless>>> = flow {
        emit(Resource.Loading())

        roomDataSource.getHomelesses().collect { emit(it) } // Emit updated Room data

//        if (networkManager.isNetworkConnected()) {
//            try {
//                val firestoreHomelesses = firestoreDataSource.getHomelesses()
//                when (firestoreHomelesses) {
//                    is Resource.Success -> {
//                        fetchHomelessesFromFirestoreToRoom(firestoreHomelesses.data!!)
//                        roomDataSource.getHomelesses().collect { emit(it) } // Emit updated Room data
//                    }
//                    is Resource.Error -> {
//                        emit(Resource.Error(firestoreHomelesses.message!!))
//                    }
//                    is Resource.Loading -> {}
//                }
//            } catch (e: Exception) {
//                emit(Resource.Error("Errore durante la sincronizzazione: ${e.message}"))
//            }
//        } else {// 2. If offline, fetch from Room
//            roomDataSource.getHomelesses().collect { emit(it) }
//        }
    }

    suspend fun getHomelessById(homelessID: String): Homeless? {
        return roomDataSource.getHomelessById(homelessID)
    }

    suspend fun updateHomeless(homeless: Homeless): Resource<Homeless> {
        return try {
            roomDataSource.updateHomeless(homeless)
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.updateHomeless(homeless)
                if (resource is Resource.Success) {
                    Resource.Success(homeless)
                } else {
                    queueSyncAction("Homeless", "update", homeless)
                    Resource.Error("Errore imprevisto. Dati salvati localmente e messi in coda per la sincronizzazione")
                }
            } else {
                queueSyncAction("Homeless", "update", homeless)
                Resource.Error("Nessuna connessione di rete. Dati salvati localmente e messi in coda per la sincronizzazione")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, dati NON salvati localmente: ${e.message}")
        }
    }

    suspend fun fetchHomelessesFromFirestoreToRoom(){
        if (networkManager.isNetworkConnected()){
            val firestoreHomelessListResource = firestoreDataSource.getHomelesses()

            if (firestoreHomelessListResource is Resource.Success) {
                val firestoreHomelessList = firestoreHomelessListResource.data!!
                firestoreHomelessList.forEach { remoteHomeless ->
                    roomDataSource.insertOrUpdateHomeless(remoteHomeless)
                }

                if (roomDataSource.isSyncQueueEmpty()) {
                    val localHomelessList = roomDataSource.getHomelessesSnapshot()
                    //Delete entries that exist locally but not in Firestore
                    val homelessIdsToDelete =
                        localHomelessList.map { it.id }.minus(firestoreHomelessList.map { it.id }
                            .toSet())
                    for (homelessId in homelessIdsToDelete) {
                        roomDataSource.deleteHomelessById(homelessId)
                    }
                }
            }
        }
    }
}