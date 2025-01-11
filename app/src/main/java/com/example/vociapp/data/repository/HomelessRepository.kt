package com.example.vociapp.data.repository

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomelessRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,   // Remote data source
    private val roomDataSource: RoomDataSource,             // Local data source
    private val networkManager: NetworkManager,             // Network manager for online/offline check
) {
    // Add new Homeless (both locally and remotely)
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
                    roomDataSource.addSyncAction("Homeless", "add", homeless)
                    Resource.Error("Errore. \nDati salvati localmente in attesa di sincronizzazione.")
                }
            } else {
                // 4. If offline, queue for sync
                roomDataSource.addSyncAction("Homeless", "add", homeless)
                Resource.Error("Rete non disponibile.\nDati salvati localmente in attesa di sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

    // Update existing Homeless (both locally and remotely)
    suspend fun updateHomeless(homeless: Homeless): Resource<Homeless> {
        return try {
            // 1. Update in room
            roomDataSource.updateHomeless(homeless)

            // 2. If online, sync with firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.updateHomeless(homeless)
                if (resource is Resource.Success) {
                    Resource.Success(homeless)
                } else {
                    // 3. If firestore update failed, queue for sync
                    roomDataSource.addSyncAction("Homeless", "update", homeless)
                    Resource.Error("Errore imprevisto. Dati salvati localmente e messi in coda per la sincronizzazione")
                }
            } else {
                // 4. If offline, queue for sync
                roomDataSource.addSyncAction("Homeless", "update", homeless)
                Resource.Error("Nessuna connessione di rete. Dati salvati localmente e messi in coda per la sincronizzazione")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, dati NON salvati localmente: ${e.message}")
        }
    }

    //get homeless from room
    fun getHomelesses(): Flow<Resource<List<Homeless>>> = flow {
        emit(Resource.Loading())
        roomDataSource.getHomelesses().collect { emit(it) }
    }

    // get homeless by id from room
    suspend fun getHomelessById(homelessID: String): Resource<Homeless> {
        return try {
            val homeless = roomDataSource.getHomelessById(homelessID)
            if (homeless != null) {
                Resource.Success(homeless)
            } else {
                Resource.Error("Senzatetto non trovato")
            }
        } catch (e: Exception) {
            Resource.Error("Senzatetto non trovato")
        }
    }

    //get homeless locations from room
    fun getLocations(): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading())
        roomDataSource.getHomelessesLocations().collect { emit(it) }
    }

    // Method to sync pending actions when the device is online
    suspend fun syncPendingActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            roomDataSource.getPendingSyncActions(System.currentTimeMillis()).collect{ pendingActions ->

                for (action in pendingActions) {
                    if (action.entityType == "Homeless"){
                        // Deserialize the data
                        val data = Gson().fromJson(action.data, Homeless::class.java)

                        when (action.operation) {
                            "add" -> firestoreDataSource.addHomeless(data)
                            "update" -> firestoreDataSource.updateHomeless(data)
                            "delete" -> firestoreDataSource.deleteHomelessById(data.id)
                        }
                        // Once synced, remove the action from the queue
                        roomDataSource.deleteSyncAction(action)
                    }
                }
            }
        }
    }

    // Update the local database with the latest data from Firestore
    suspend fun fetchHomelessesFromFirestoreToRoom(){
        // Only attempt to fetch if the device is online
        if (networkManager.isNetworkConnected()){
            val firestoreHomelessListResource = firestoreDataSource.getHomelesses()

            if (firestoreHomelessListResource is Resource.Success) {
                val firestoreHomelessList = firestoreHomelessListResource.data!!
                firestoreHomelessList.forEach { remoteHomeless ->
                    roomDataSource.insertOrUpdateHomeless(remoteHomeless)
                }
                // Only delete if the syncronization from room to firestore has already happened
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