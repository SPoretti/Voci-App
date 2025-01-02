package com.example.vociapp.data.repository

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.SyncAction
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val roomDataSource: RoomDataSource,
    private val networkManager: NetworkManager,
    private val syncQueueDao: SyncQueueDao
) {

    suspend fun addRequest(request: Request): Resource<Request> {
        return try {
            // 1. Add to Room
            roomDataSource.insertRequest(request)
            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.addRequest(request)
                if (resource is Resource.Success) {
                    return Resource.Success(request)
                } else {
                    queueSyncAction("Request", "add", request)
                    return Resource.Error("Errore imprevisto. Dati salvati localmente e messi in coda per la sincronizzazione")
                }
            } else {// 3. If offline, queue for sync
                queueSyncAction("Request", "add", request)
                Resource.Error("Nessuna connessione di rete. Dati salvati localmente e messi in coda per la sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, dati NON salvati localmente: ${e.message}")
        }
    }

    fun getRequests(): Flow<Resource<List<Request>>> = flow {
        emit(Resource.Loading())
        // 1. If online, fetch from Firestore and update Room
        if (networkManager.isNetworkConnected()) {
            try {
//                val firestoreRequests = firestoreDataSource.getRequests()
//                when (firestoreRequests) {
//                    is Resource.Success -> {
                        //fetchRequestsFromFirestoreToRoom()
                        roomDataSource.getRequests().collect { emit(it) }
//                    }
//                    is Resource.Error -> {
//                        emit(Resource.Error(firestoreRequests.message!!))
//                    }
//                    is Resource.Loading -> {}
//                }
            } catch (e: Exception) {
                emit(Resource.Error("Errore durante la sincronizzazione: ${e.message}"))
            }
        } else {// 2. If offline, fetch from Room
            roomDataSource.getRequests().collect { emit(it) }
        }
    }

    suspend fun updateRequest(request: Request): Resource<Request> {
        return try {
            roomDataSource.updateRequest(request)
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.updateRequest(request)
                if (resource is Resource.Success) {
                    Resource.Success(request)
                } else {
                    queueSyncAction("Request", "update", request)
                    Resource.Error("Errore imprevisto. Dati salvati localmente e messi in coda per la sincronizzazione")
                }
            } else {
                queueSyncAction("Request", "update", request)
                Resource.Error("Nessuna connessione di rete. Dati salvati localmente e messi in coda per la sincronizzazione")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, dati NON salvati localmente: ${e.message}")
        }
    }

    suspend fun deleteRequest(request: Request): Resource<Unit> {
        return try {
            roomDataSource.deleteRequestById(request.id) // Delete locally first
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.deleteRequest(request)
                if (resource is Resource.Success) {
                    resource // Return success if Firestore deletion succeeds
                } else {
                    queueSyncAction("Request", "delete", request)
                    Resource.Error("Errore imprevisto. Dati eliminati localmente e messi in coda per la sincronizzazione")
                }
            } else {
                queueSyncAction("Request", "delete", request)
                Resource.Error("Nessuna connessione di rete. Dati eliminati localmente e messi in coda per la sincronizzazione")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, dati NON eliminati localmente: ${e.message}")
        }
    }

    suspend fun getRequestById(requestId: String): Resource<Request> {
        try {
            // 1. Try to get the request from the local database first
            val localRequest = roomDataSource.getRequestById(requestId)

            // 2. If found locally, return it
            if (localRequest != null) {
                return Resource.Success(localRequest)
            }

            // 3. If not found locally and network is available, fetch from Firestore
            if (networkManager.isNetworkConnected()) {
                val firestoreResource = firestoreDataSource.getRequestById(requestId)
                if (firestoreResource is Resource.Success) {
                    // 4. Cache the fetched request locally
                    roomDataSource.insertRequest(firestoreResource.data!!)
                    return firestoreResource
                } else {
                    // 5. Handle Firestore error
                    return firestoreResource // Or you can re-throw or wrap the exception
                }
            } else {
                // 6. Handle offline scenario
                return Resource.Error("Nessuna connessione di rete. Dati non disponibili localmente.")
            }
        } catch (e: Exception) {
            // 7. Handle unexpected errors
            return Resource.Error("Errore durante il recupero della richiesta: ${e.message}")
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

    suspend fun fetchRequestsFromFirestoreToRoom(){
        if (networkManager.isNetworkConnected()){
            val firestoreRequestListResource = firestoreDataSource.getRequests()

            if (firestoreRequestListResource is Resource.Success){
                val firestoreRequestList = firestoreRequestListResource.data!!

                firestoreRequestList.forEach { remoteRequest ->
                    roomDataSource.insertOrUpdateRequest(remoteRequest)
                }

                if (roomDataSource.syncQueueDao.isEmpty()) {
                    val localRequestList = roomDataSource.getRequestsSnapshot()
                    //Delete entries that exist locally but not in Firestore
                    val requestIdsToDelete =
                        localRequestList.map { it.id }.minus(firestoreRequestList.map { it.id }
                            .toSet())
                    for (requestId in requestIdsToDelete) {
                        roomDataSource.deleteRequestById(requestId)
                    }
                }
            }
        }
    }
}