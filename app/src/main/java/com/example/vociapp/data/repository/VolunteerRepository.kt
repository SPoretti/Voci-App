package com.example.vociapp.data.repository

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.database.Converters
import com.example.vociapp.data.local.database.SyncAction
import com.google.gson.Gson
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
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
    suspend fun addVolunteer(volunteer: Volunteer): Resource<String> {
        //Add locally
        roomDataSource.insertVolunteer(volunteer)
        return if (networkManager.isNetworkConnected()) {
            // Online: sync with Firestore
            firestoreDataSource.addVolunteer(volunteer)
        } else {
            // Offline: add to sync queue for later
            queueSyncAction("volunteer", "add", volunteer)
            Resource.Success("No network connection. Data saved locally and queued for later sync.")
        }
    }

    // Get volunteer by ID, prefer local database if offline
    fun getVolunteerById(id: String): Flow<Resource<Volunteer>> = flow {
        emit(Resource.Loading())
        if (networkManager.isNetworkConnected()) {
            try {
                val volunteer = firestoreDataSource.getVolunteerById(id)
                if (volunteer != null) {
                    emit(Resource.Success(volunteer))
                    // Cache in local database for offline use
                    roomDataSource.insertVolunteer(volunteer)
                } else {
                    emit(Resource.Error("Volunteer not found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error fetching data: ${e.localizedMessage}"))
            }
        } else {
            // If offline, fetch from local database
            val volunteer = roomDataSource.getVolunteerById(id)
            if (volunteer != null) {
                emit(Resource.Success(volunteer))
            } else {
                emit(Resource.Error("No data available offline"))
            }
        }
    }

    // Update volunteer details (both locally and remotely)
    suspend fun updateVolunteer(volunteer: Volunteer): Resource<Unit> {
        roomDataSource.updateVolunteer(volunteer)
        return if (networkManager.isNetworkConnected()) {
            firestoreDataSource.updateVolunteer(volunteer)
        } else {
            // Offline: update locally and add to sync queue
            queueSyncAction("volunteer", "update", volunteer)
            Resource.Error("No network connection. Data saved locally and queued for later sync.")
        }
    }

    // Get volunteer by email, checking first from Firestore, then local if offline
    fun getVolunteerByEmail(email: String): Flow<Resource<Volunteer>> = flow {
        emit(Resource.Loading())
        if (networkManager.isNetworkConnected()) {
            try {
                val volunteer = firestoreDataSource.getVolunteerByEmail(email)
                if (volunteer != null) {
                    emit(Resource.Success(volunteer))
                    // Cache in local database for offline use
                    roomDataSource.insertVolunteer(volunteer)
                } else {
                    emit(Resource.Error("Volunteer not found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error fetching data: ${e.localizedMessage}"))
            }
        } else {
            // If offline, fetch from local database
            val volunteer = roomDataSource.getVolunteerByEmail(email)
            if (volunteer != null) {
                emit(Resource.Success(volunteer))
            } else {
                emit(Resource.Error("No data available offline"))
            }
        }
    }

    suspend fun getVolunteerIdByEmail(email: String): String? {
        return if (networkManager.isNetworkConnected()) {
            // Fetch from remote if connected
            firestoreDataSource.getVolunteerIdByEmail(email)
        } else {
            // Fetch from local otherwise
            roomDataSource.getVolunteerIdByEmail(email)
        }
    }

    fun getVolunteerByNickname(nickname: String): Flow<Resource<Volunteer>> = flow {
        emit(Resource.Loading()) // Indicate loading state

        try {
            val volunteer: Volunteer? =
                if (networkManager.isNetworkConnected()) {
                    // Fetch from remote if network is available
                    firestoreDataSource.getVolunteerByNickname(nickname)
                        ?.also { fetchedVolunteer ->
                            // Save to local database for offline access
                            roomDataSource.insertVolunteer(fetchedVolunteer)
                        }
                } else {
                    // Fetch from local database if offline
                    roomDataSource.getVolunteerByNickname(nickname)
                }

            if (volunteer != null) {
                emit(Resource.Success(volunteer)) // Emit success with the retrieved volunteer
            } else {
                emit(Resource.Error("Volunteer not found")) // Emit error if volunteer not found
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching volunteer by nickname: ${e.localizedMessage}"))
        }
    }

    fun getUserPreferences(userId: String): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading()) // Indicate loading state

        try {
            if (networkManager.isNetworkConnected()) {
                // Fetch preferences from Firestore if network is connected
                val remoteResult = firestoreDataSource.getUserPreferences(userId)
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
                val newVolunteer = currentVolunteer?.copy(preferredHomelessIds = preferredHomelessIds)
                roomDataSource.updateUserPreferences(userId, preferredHomelessIds)
                queueSyncAction("Volunteer", "updatePreferences", newVolunteer!!)
                Resource.Error("No network connection. Data saved locally and queued for later sync.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred while updating user preferences")
        }
    }


    // Sync actions from the sync queue
    suspend fun syncPendingActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            syncQueueDao.getPendingSyncActions(System.currentTimeMillis()).collect { pendingActions ->

                for (action in pendingActions) {
                    val data = Gson().fromJson(action.data, Volunteer::class.java)

                    when (action.operation) {
                        "add" -> firestoreDataSource.addVolunteer(data)
                        "update" -> firestoreDataSource.updateVolunteer(data)
                        "updatePreferences" -> firestoreDataSource.updateUserPreferences(data.id, data.preferredHomelessIds)
                        //"delete" -> firestoreDataSource.deleteVolunteer(data.id)
                    }

                    // Once synced, remove the action from the queue
                    syncQueueDao.deleteSyncAction(action)
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
