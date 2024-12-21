package com.example.vociapp.data.repository

import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.dao.SyncQueueDao
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
            Resource.Success("Volunteer added offline.")
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
        return if (networkManager.isNetworkConnected()) {
            firestoreDataSource.updateVolunteer(volunteer)
        } else {
            // Offline: update locally and add to sync queue
            roomDataSource.updateVolunteer(volunteer)
            queueSyncAction("volunteer", "update", volunteer)
            Resource.Success(Unit)
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

    // Sync actions from the sync queue
    suspend fun syncPendingActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            val pendingActions = syncQueueDao.getPendingSyncActions(System.currentTimeMillis())

            for (action in pendingActions) {
                val data = Gson().fromJson(action.data, Volunteer::class.java)

                when (action.operation) {
                    "add" -> firestoreDataSource.addVolunteer(data)
                    "update" -> firestoreDataSource.updateVolunteer(data)
                    "delete" -> firestoreDataSource.deleteVolunteer(data.id)
                }

                // Once synced, remove the action from the queue
                syncQueueDao.deleteSyncAction(action)
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




}