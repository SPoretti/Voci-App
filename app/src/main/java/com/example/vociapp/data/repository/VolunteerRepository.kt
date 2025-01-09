package com.example.vociapp.data.repository

import android.util.Log
import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.database.Converters
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.SyncAction
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VolunteerRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val roomDataSource: RoomDataSource,  // Add local data source
    private val networkManager: NetworkManager,    // Add network manager for online/offline check
    private val syncQueueDao: SyncQueueDao        // Add sync queue to handle offline changes
) {

    // Add a new volunteer and sync if needed
    suspend fun addVolunteer(volunteer: Volunteer): Resource<Volunteer> {
        return try {
            // 1. Add to Room
            roomDataSource.insertVolunteer(volunteer)
            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.addVolunteer(volunteer)
                if (resource is Resource.Success) {
                    Resource.Success(volunteer)
                } else {
                    queueSyncAction("Volunteer", "add", volunteer)
                    Resource.Error("Errore imprevisto. Dati salvati localmente e messi in coda per la sincronizzazione")
                }
            } else {
                // 3. If offline, queue for sync
                queueSyncAction("Volunteer", "add", volunteer)
                Resource.Error("Nessuna connessione di rete. Dati salvati localmente e messi in coda per la sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, dati NON salvati localmente: ${e.message}")
        }
    }

    // Get volunteer by ID, prefer local database if offline
    suspend fun getVolunteerById(volunteerId: String): Resource<Volunteer> {
        try {
            // 1. Try to get the request from the local database first
            val localVolunteer = roomDataSource.getVolunteerById(volunteerId)

            // 2. If found locally, return it
            if (localVolunteer != null) {
                return Resource.Success(localVolunteer)
            }

            // 3. If not found locally and network is available, fetch from Firestore
            if (networkManager.isNetworkConnected()) {
                val firestoreResource = firestoreDataSource.getVolunteerById(volunteerId)
                if (firestoreResource is Resource.Success) {
                    // 4. Cache the fetched volunteer locally
                    roomDataSource.insertVolunteer(firestoreResource.data!!)
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
            return Resource.Error("Errore durante il recupero del volontario: ${e.message}")
        }

    }

    // Get volunteer by email, checking first from Firestore, then local if offline
    suspend fun getVolunteerByEmail(email: String): Resource<Volunteer> {
        try {
            // 1. Try to get the request from the local database first
            val localVolunteer = roomDataSource.getVolunteerByEmail(email)

            // 2. If found locally, return it
            if (localVolunteer != null) {
                return Resource.Success(localVolunteer)
            }

            // 3. If not found locally and network is available, fetch from Firestore
        if (networkManager.isNetworkConnected()) {
            val firestoreResource = firestoreDataSource.getVolunteerByEmail(email)
            if (firestoreResource is Resource.Success) {
                // Cache in local database for offline use
                roomDataSource.insertVolunteer(firestoreResource.data!!)
                return firestoreResource
                } else {
                    // 5. Handle Firestore error
                    return firestoreResource // Or you can re-throw or wrap the exception
                }
            }  else {
            // 6. Handle offline scenario
            return Resource.Error("Nessuna connessione di rete. Dati non disponibili localmente.")
            }
        } catch (e: Exception) {
            // 7. Handle unexpected errors
            return Resource.Error("Errore durante il recupero della richiesta: ${e.message}")
        }
    }

    // Update volunteer details (both locally and remotely)
    suspend fun updateVolunteer(volunteer: Volunteer): Resource<Unit> {
        roomDataSource.updateVolunteer(volunteer)
        return if (networkManager.isNetworkConnected()) {
            firestoreDataSource.updateVolunteer(volunteer)
        } else {
            // Offline: update locally and add to sync queue
            queueSyncAction("Volunteer", "update", volunteer)
            Resource.Error("No network connection. Data saved locally and queued for later sync.")
        }
    }

    suspend fun getVolunteerByNickname(nickname: String): Resource<Volunteer> {
        try {
            return if (networkManager.isNetworkConnected()) {
                val firestoreResource = firestoreDataSource.getVolunteerByNickname(nickname)
                if (firestoreResource is Resource.Success) {
                    // 4. Cache the fetched request locally
                    firestoreResource
                } else {
                    // 5. Handle Firestore error
                    firestoreResource // Or you can re-throw or wrap the exception
                }
            } else {
                // 6. Handle offline scenario
                Resource.Error("Nessuna connessione di rete. Dati non disponibili localmente.")
            }
        } catch (e: Exception) {
            // 7. Handle unexpected errors
            return Resource.Error("Errore durante il recupero del volontario: ${e.message}")
        }
    }

    fun getUserPreferences(userId: String): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading()) // Indicate loading state

        try {
            if (networkManager.isNetworkConnected()) {
                // Fetch preferences from Firestore if network is connected
                val remoteResult = firestoreDataSource.getUserPreferences(userId)
                Log.d("UserPreferences", "Remote result: $remoteResult")
                if (remoteResult is Resource.Success) {
                    // Save preferences to local database for offline access
                    roomDataSource.updateUserPreferences(userId, remoteResult.data!!)
                }
                emit(remoteResult)
            } else {
                // Fetch preferences from local database if offline
                val jsonPreferences = roomDataSource.getUserPreferences(userId).data
                val preferences = Converters().fromJson(jsonPreferences!!)
                emit(Resource.Success(preferences))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred while retrieving user preferences"))
        }
    }

    suspend fun updateUserPreferences(userId: String, preferredHomelessIds: List<String>): Resource<Unit> {
        return try {
            if (networkManager.isNetworkConnected()) {
                // Update Firestore if connected
                val remoteResult = firestoreDataSource.updateUserPreferences(userId, preferredHomelessIds)
                if (remoteResult is Resource.Success) {
                    roomDataSource.updateUserPreferences(userId, preferredHomelessIds)
                }
                remoteResult // Return the result from Firestore
            } else {
                val currentVolunteer = roomDataSource.getVolunteerById(userId)
                val newVolunteer = currentVolunteer.copy(preferredHomelessIds = preferredHomelessIds)
                roomDataSource.updateUserPreferences(userId, preferredHomelessIds)
                queueSyncAction("Volunteer", "update_preferences", newVolunteer!!)
                Resource.Error("No network connection. Data saved locally and queued for later sync.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred while updating user preferences")
        }
    }

    suspend fun fetchVolunteersFromFirestoreToRoom(){
        if (networkManager.isNetworkConnected()){
            val firestoreVolunteersListResource = firestoreDataSource.getVolunteers()

            if (firestoreVolunteersListResource is Resource.Success) {
                val firestoreVolunteersList = firestoreVolunteersListResource.data!!

                firestoreVolunteersList.forEach { remoteVolunteer ->
                    roomDataSource.insertOrUpdateVolunteer(remoteVolunteer)
                }

                if (roomDataSource.syncQueueDao.isEmpty()) {
                    val localVolunteerList = roomDataSource.getVolunteersSnapshot()
                    //Delete entries that exist locally but not in Firestore
                    val volunteerIdsToDelete =
                        localVolunteerList.map { it.id }.minus(firestoreVolunteersList.map { it.id }
                            .toSet())
                    for (volunteerId in volunteerIdsToDelete) {
                        roomDataSource.deleteVolunteerById(volunteerId)
                    }
                }
            }
        }
    }

    // Sync actions from the sync queue
    suspend fun syncPendingActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            syncQueueDao.getPendingSyncActions(System.currentTimeMillis()).collect { pendingActions ->

                for (action in pendingActions) {

                    if (action.entityType == "Volunteer"){
                        val data = Gson().fromJson(action.data, Volunteer::class.java)
                        Log.d("SyncPendingActions1", "Syncing action: $data")
                        when (action.operation) {
                            "add" -> firestoreDataSource.addVolunteer(data)
                            "update" -> firestoreDataSource.updateVolunteer(data)
                            "update_preferences" -> {
                                //Log.d("SyncPendingActions", "Updating user preferences: ${data.preferredHomelessIds}")
                                firestoreDataSource.updateUserPreferences(
                                    data.id,
                                    data.preferredHomelessIds
                                )
                            }//"delete" -> firestoreDataSource.deleteVolunteer(data.id)
                            else -> {
                                Log.d(
                                    "SyncPendingActions",
                                    "Unknown operation: ${action.operation}"
                                )
                            }
                        }
                        // Once synced, remove the action from the queue
                        syncQueueDao.deleteSyncAction(action)
                    }
                }
            }
        }
    }

    // Add to the sync queue for offline actions
    private suspend fun queueSyncAction(entityType: String, operation: String, data: Any) {
        val dataJson = Gson().toJson(data)
        val syncAction = SyncAction(
            entityType = entityType,
            operation = operation,
            data = dataJson,
            timestamp = System.currentTimeMillis()
        )
        syncQueueDao.addSyncAction(syncAction)
    }
}
