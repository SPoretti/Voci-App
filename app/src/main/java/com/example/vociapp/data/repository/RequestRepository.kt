package com.example.vociapp.data.repository

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,   // Remote data source
    private val roomDataSource: RoomDataSource,             // Local data source
    private val networkManager: NetworkManager,             // Network manager for online/offline check
) {
    // Add new Request (both locally and remotely)
    suspend fun addRequest(request: Request): Resource<Request> {
        return try {
            // 1. Add to Room
            roomDataSource.insertRequest(request)

            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.addRequest(request)
                if (resource is Resource.Success) {
                    Resource.Success(request)
                } else {
                    // 3. If firestore addition failed, queue for sync
                    roomDataSource.addSyncAction("Request", "add", request)
                    Resource.Error("Errore. \nDati salvati localmente in attesa di sincronizzazione.")
                }
            } else {
                // 4. If offline, queue for sync
                roomDataSource.addSyncAction("Request", "add", request)
                Resource.Error("Rete non disponibile.\nDati salvati localmente in attesa di sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

    // Update existing Request (both locally and remotely)
    suspend fun updateRequest(request: Request): Resource<Request> {
        return try {
            // 1. Update in Room
            roomDataSource.updateRequest(request)

            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.updateRequest(request)
                if (resource is Resource.Success) {
                    Resource.Success(request)
                } else {
                    // 3. If firestore update failed, queue for sync
                    roomDataSource.addSyncAction("Request", "update", request)
                    Resource.Error("Errore imprevisto. Dati salvati localmente e messi in coda per la sincronizzazione")
                }
            } else {
                // 4. If offline, queue for sync
                roomDataSource.addSyncAction("Request", "update", request)
                Resource.Error("Nessuna connessione di rete. Dati salvati localmente e messi in coda per la sincronizzazione")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, dati NON salvati localmente: ${e.message}")
        }
    }

    //get requests from room and wrap the result in a resource
    fun getRequests(): Flow<Resource<List<Request>>> = flow {
        try {
            // Indicate loading state
            emit(Resource.Loading())
            roomDataSource.getRequests().collect { requestList ->
                // Emit success with the fetched list
                emit(Resource.Success(requestList))
            }
        } catch (e: Exception) {
            //Emit error if there's an issue
            emit(Resource.Error("Error fetching requests from local data: ${e.localizedMessage}"))
        }
    }

    //delete request from room and wrap the result in a resource
    suspend fun deleteRequest(request: Request): Resource<Unit> {
        return try {
            // 1. Delete from room
            roomDataSource.deleteRequestById(request.id)

            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.deleteRequest(request)
                if (resource is Resource.Success) {
                    resource // Return success if Firestore deletion succeeds
                } else {
                    // 3. If firestore deletion failed, queue for sync
                    roomDataSource.addSyncAction("Request", "delete", request)
                    Resource.Error("Errore imprevisto. Dati eliminati localmente e messi in coda per la sincronizzazione")
                }
            } else {
                // 4. if offline, queue for sync
                roomDataSource.addSyncAction("Request", "delete", request)
                Resource.Error("Nessuna connessione di rete. Dati eliminati localmente e messi in coda per la sincronizzazione")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, dati NON eliminati localmente: ${e.message}")
        }
    }

    suspend fun getRequestById(requestId: String): Resource<Request> {
        return try {
            // 1. Try to get the request from room
            Resource.Success(roomDataSource.getRequestById(requestId))
        } catch (e: Exception) {
            // 2. Handle unexpected errors
            Resource.Error("Errore durante il recupero della richiesta: ${e.message}")
        }
    }

    fun getRequestsByHomelessId(homelessId: String): Flow<Resource<List<Request>>> = flow {
        try {
            emit(Resource.Loading()) // Indicate loading state
            roomDataSource.getRequestsByHomelessId(homelessId).collect { requestList ->
                emit(Resource.Success(requestList)) // Emit success with the fetched list
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching requests from local data: ${e.localizedMessage}"))
        }
    }

    // Add a new sync action to the queue
    suspend fun syncPendingActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            roomDataSource.getPendingSyncActions(System.currentTimeMillis()).collect{ pendingActions ->

                for (action in pendingActions) {
                    if (action.entityType == "Request"){
                        // Deserialize the data
                        val data = Gson().fromJson(action.data, Request::class.java)

                        when (action.operation) {
                            "add" -> firestoreDataSource.addRequest(data)
                            "update" -> firestoreDataSource.updateRequest(data)
                            "delete" -> firestoreDataSource.deleteRequest(data)
                        }
                        // Once synced, remove the action from the queue
                        roomDataSource.deleteSyncAction(action)
                    }
                }
            }
        }
    }

    // Update the local database with the latest data from Firestore
    suspend fun fetchRequestsFromFirestoreToRoom(){
        // Only attempt to fetch if the device is online
        if (networkManager.isNetworkConnected()){
            val firestoreRequestListResource = firestoreDataSource.getRequests()

            if (firestoreRequestListResource is Resource.Success){
                val firestoreRequestList = firestoreRequestListResource.data!!

                firestoreRequestList.forEach { remoteRequest ->
                    roomDataSource.insertOrUpdateRequest(remoteRequest)
                }
                // Only delete if the syncronization from room to firestore has already happened
                if (roomDataSource.isSyncQueueEmpty()) {
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