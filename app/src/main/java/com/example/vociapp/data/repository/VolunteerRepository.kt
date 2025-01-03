package com.example.vociapp.data.repository

import android.util.Log
import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.database.Converters
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
) {

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
                    // 3. If firestore addition failed, queue for sync
                    queueSyncAction("Volunteer", "add", volunteer)
                    Resource.Error("Errore. \nDati salvati localmente in attesa di sincronizzazione.")
                }
            } else {
                // 4. If offline, queue for sync
                queueSyncAction("Volunteer", "add", volunteer)
                Resource.Error("Rete non disponibile.\nDati salvati localmente in attesa di sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

    suspend fun getVolunteerById(id: String): Volunteer? {
        return roomDataSource.getVolunteerById(id)
    }

    // Get volunteer by ID, prefer local database if offline
//    fun getVolunteerById(id: String): Flow<Resource<Volunteer>> = flow {
//        emit(Resource.Loading())
//        if (networkManager.isNetworkConnected()) {
//            try {
//                val volunteer = firestoreDataSource.getVolunteerById(id)
//                if (volunteer != null) {
//                    emit(Resource.Success(volunteer))
//                    // Cache in local database for offline use
//                    roomDataSource.insertVolunteer(volunteer)
//                } else {
//                    emit(Resource.Error("Volunteer not found"))
//                }
//            } catch (e: Exception) {
//                emit(Resource.Error("Error fetching data: ${e.localizedMessage}"))
//            }
//        } else {
//            // If offline, fetch from local database
//            val volunteer = roomDataSource.getVolunteerById(id)
//            if (volunteer != null) {
//                emit(Resource.Success(volunteer))
//            } else {
//                emit(Resource.Error("No data available offline"))
//            }
//        }
//    }

    // Update volunteer details (both locally and remotely)
    suspend fun updateVolunteer(volunteer: Volunteer): Resource<Volunteer> {
        return try {
            // 1. Update Room
            roomDataSource.updateVolunteer(volunteer)

            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.updateVolunteer(volunteer)
                if (resource is Resource.Success) {
                    Resource.Success(volunteer)
                } else {
                    // 3. If firestore update failed, queue for sync
                    queueSyncAction("Volunteer", "update", volunteer)
                    Resource.Error("Errore. \nDati salvati localmente in attesa di sincronizzazione.")
                }
            } else {
                // 4. If offline, queue for sync
                queueSyncAction("Volunteer", "update", volunteer)
                Resource.Error("Rete non disponibile.\nDati salvati localmente in attesa di sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
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
            val volunteerId = firestoreDataSource.getVolunteerIdByEmail(email)
            if (volunteerId != null){
                val volunteer = firestoreDataSource.getVolunteerById(volunteerId)
                if (volunteer != null) {
                    roomDataSource.insertVolunteer(volunteer)
                }
            }
            volunteerId
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

//    fun getUserPreferences(userId: String): Flow<Resource<List<String>>> = flow {
//        emit(Resource.Loading()) // Indicate loading state
//
//        try {
//            if (networkManager.isNetworkConnected()) {
//                // Fetch preferences from Firestore if network is connected
//                val remoteResult = firestoreDataSource.getUserPreferences(userId)
//                if (remoteResult is Resource.Success) {
//                    // Save preferences to local database for offline access
//                    roomDataSource.updateUserPreferences(userId, remoteResult.data!!)
//                }
//                emit(remoteResult)
//            } else {
//                // Fetch preferences from local database if offline
//                val jsonPreferences = roomDataSource.getUserPreferences(userId).data
//                val preferences = Converters().fromJson(jsonPreferences!!)
//                emit(Resource.Success(preferences))
//            }
//        } catch (e: Exception) {
//            emit(Resource.Error(e.message ?: "An error occurred while retrieving user preferences"))
//        }
//    }

    fun getUserPreferences(userId: String): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading()) // Indicate loading state
        try {
            //1. Get from Room, deserialize, emit
            val jsonPreferences = roomDataSource.getUserPreferences(userId).data
            //2. Deserialize the data
            val preferences = Converters().fromJson(jsonPreferences!!)
            //3. Emit the data
            emit(Resource.Success(preferences))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred while retrieving user preferences"))
        }
    }

    suspend fun updateUserPreferences(userId: String, preferredHomelessIds: List<String>): Resource<Unit> {
        return try {
            roomDataSource.updateUserPreferences(userId, preferredHomelessIds)

            if (networkManager.isNetworkConnected()) {
                // Update Firestore if connected
                val resource = firestoreDataSource.updateUserPreferences(userId, preferredHomelessIds)
                if (resource is Resource.Success) {
                    Resource.Success(Unit)
                }
                else{
                    queueSyncAction("Volunteer", "update_preferences", resource)
                    Resource.Error("Errore.\nDati salvati localmente in attesa di sincronizzazione.")
                }

            } else {
                val volunteer = roomDataSource.getVolunteerById(userId)
                queueSyncAction("Volunteer", "update_preferences", volunteer!!)
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
            roomDataSource.getPendingSyncActions(System.currentTimeMillis()).collect { pendingActions ->

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
                        roomDataSource.deleteSyncAction(action)
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
        roomDataSource.addSyncAction(syncAction)
    }

    suspend fun fetchVolunteersFromFirestoreToRoom(){
        if (networkManager.isNetworkConnected()){
            val firestoreVolunteersListResource = firestoreDataSource.getVolunteers()

            if (firestoreVolunteersListResource is Resource.Success) {
                val firestoreVolunteersList = firestoreVolunteersListResource.data!!

                firestoreVolunteersList.forEach { remoteVolunteer ->
                    roomDataSource.insertOrUpdateVolunteer(remoteVolunteer)
                }

                if (roomDataSource.isSyncQueueEmpty()) {
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
}
