package com.example.vociapp.data.repository

import android.util.Log
import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.database.Preference
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.data.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VolunteerRepository @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,   // Remote data source
    private val roomDataSource: RoomDataSource,             // Local data source
    private val networkManager: NetworkManager,             // Network manager for online/offline check
) {
    // Add new Volunteer (both locally and remotely)
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
                    roomDataSource.addSyncAction("Volunteer", "add", volunteer)
                    Resource.Error("Errore. \nDati salvati localmente in attesa di sincronizzazione.")
                }
            } else {
                // 4. If offline, queue for sync
                roomDataSource.addSyncAction("Volunteer", "add", volunteer)
                Resource.Error("Rete non disponibile.\nDati salvati localmente in attesa di sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

    // Update existing Volunteer (both locally and remotely)
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
                    roomDataSource.addSyncAction("Volunteer", "update", volunteer)
                    Resource.Error("Errore. \nDati salvati localmente in attesa di sincronizzazione.")
                }
            } else {
                // 4. If offline, queue for sync
                roomDataSource.addSyncAction("Volunteer", "update", volunteer)
                Resource.Error("Rete non disponibile.\nDati salvati localmente in attesa di sincronizzazione.")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

    suspend fun getVolunteerById(id: String): Resource<Volunteer?> {
        return try {
            val volunteer = roomDataSource.getVolunteerById(id)
            if (volunteer != null)
                Resource.Success(volunteer)
            else
                Resource.Error("Volontario non trovato, provare a refreshare")
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

    suspend fun getVolunteerByEmail(email: String):Resource<Volunteer?> {
        return try {
            val volunteer = roomDataSource.getVolunteerByEmail(email)
            if (volunteer != null)
                Resource.Success(volunteer)
            else
                Resource.Error("Volontario non trovato, provare a refreshare")
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

//    suspend fun getVolunteerIdByEmail(email: String): Resource<String?> {
//        return try {
//            val volunteerId = roomDataSource.getVolunteerByEmail(email)
//            if (volunteerId != null)
//                Resource.Success(volunteerId)
//            else
//                Resource.Error("Volontario non trovato, provare a refreshare")
//        } catch (e: Exception) {
//            Resource.Error("Errore, operazione fallita: ${e.message}")
//        }
//    }

    suspend fun getVolunteerByNickname(nickname: String): Resource<Volunteer?> {
        return try{
            val volunteer = roomDataSource.getVolunteerByNickname(nickname)
            if (volunteer != null)
                Resource.Success(volunteer)
            else
                Resource.Error("Volontario non trovato, provare a refreshare")
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

    // Sync volunteer actions from the sync queue
    suspend fun syncPendingVolunteerActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            roomDataSource.getPendingSyncActions(System.currentTimeMillis()).collect { pendingActions ->

                for (action in pendingActions) {

                    if (action.entityType == "Volunteer"){
                        // Deserialize the data
                        val data = Gson().fromJson(action.data, Volunteer::class.java)

                        when (action.operation) {
                            "add" -> firestoreDataSource.addVolunteer(data)
                            "update" -> firestoreDataSource.updateVolunteer(data)
                            //"delete" -> firestoreDataSource.deleteVolunteer(data.id)
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

    // Update the local database with the latest data from Firestore
    suspend fun fetchVolunteersFromFirestoreToRoom(){
        // Only attempt to fetch if the device is online
        if (networkManager.isNetworkConnected()){
            val firestoreVolunteersListResource = firestoreDataSource.getVolunteers()

            if (firestoreVolunteersListResource is Resource.Success) {
                val firestoreVolunteersList = firestoreVolunteersListResource.data!!

                firestoreVolunteersList.forEach { remoteVolunteer ->
                    roomDataSource.insertOrUpdateVolunteer(remoteVolunteer)
                }
                // Only delete if the syncronization from room to firestore has already happened
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


    //-----------------------------------------Preferences------------------------------------
    // Add a preference (syncs to both Room and Firestore)
    suspend fun addPreference(preference: Preference): Resource<Unit>{
        return try {
            // 1. Add to Room
            roomDataSource.insertPreference(preference)

            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.addPreference(preference)
                if (resource is Resource.Success) {
                    Resource.Success(Unit)
                } else {
                    // 3. If firestore addition failed, queue for sync
                    roomDataSource.addSyncAction("Preference", "add", preference)
                    Resource.Error("Errore. \nAzione eseguita localmente in attesa di sincronizzazione")
                }
            } else {
                // 4. If offline, queue for sync
                roomDataSource.addSyncAction("Preference", "add", preference)
                Resource.Error("Rete non disponibile.\nAzione eseguita localmente in attesa di sincronizzazione")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

    // Remove a preference (syncs to both Room and Firestore)
    suspend fun deletePreference(preference: Preference): Resource<Unit>  {
        return try{
            // 1. Delete from room
            roomDataSource.deletePreference(preference)
            // 2. If online, sync with Firestore
            if (networkManager.isNetworkConnected()) {
                val resource = firestoreDataSource.deletePreference(preference)
                if (resource is Resource.Success) {
                    resource // Return success if Firestore deletion succeeds
                } else {
                    // 3. If firestore deletion failed, queue for sync
                    roomDataSource.addSyncAction("Preference", "delete", preference)
                    Resource.Error("Errore imprevisto. Azione eseguita localmente in attesa di sincronizzazione")
                }
            } else {
                // 4. if offline, queue for sync
                roomDataSource.addSyncAction("Preference", "delete", preference)
                Resource.Error("Nessuna connessione di rete. Azione eseguita localmente in attesa di sincronizzazione")
            }
        } catch (e: Exception) {
            Resource.Error("Errore, azione fallita: ${e.message}")
        }
    }


    // Get preferred homeless people for a volunteer (from Room)
    fun getPreferredHomelessForVolunteer(volunteerId: String): Flow<Resource<List<Preference>>> = flow {
        try {
            // Indicate loading state
            emit(Resource.Loading())
            roomDataSource.getPreferencesForVolunteer(volunteerId).collect { requestList ->
                // Emit success with the fetched list
                emit(Resource.Success(requestList))
            }
        } catch (e: Exception) {
            //Emit error if there's an issue
            emit(Resource.Error("Error fetching requests from local data: ${e.localizedMessage}"))
        }
    }

    // Sync preferences actions from the sync queue
    suspend fun syncPendingPreferenceActions() {
        // Only attempt to sync if the device is online
        if (networkManager.isNetworkConnected()) {
            // Get all the pending sync actions from the queue
            roomDataSource.getPendingSyncActions(System.currentTimeMillis()).collect { pendingActions ->

                for (action in pendingActions) {

                    if (action.entityType == "Preference"){
                        // Deserialize the data
                        val data = Gson().fromJson(action.data, Preference::class.java)

                        when (action.operation) {
                            "add" -> firestoreDataSource.addPreference(data)
                            "delete" -> firestoreDataSource.deletePreference(data)
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

    // Update the local database with the latest data from Firestore
    suspend fun fetchPreferencesFromFirestoreToRoom(volunteerId: String){
        // Only attempt to fetch if the device is online
        if (networkManager.isNetworkConnected()){
            val firestorePreferencesListResource = firestoreDataSource.getPreferencesForVolunteer(volunteerId)

            if (firestorePreferencesListResource is Resource.Success) {
                val firestorePreferencesList = firestorePreferencesListResource.data!!

                firestorePreferencesList.forEach { remotePreference ->
                    roomDataSource.insertPreference(remotePreference)
                }
                // Only delete if the synchronization from room to firestore has already happened
                if (roomDataSource.isSyncQueueEmpty()) {
                    // 1. Get local preferences from Room
                    val localPreferenceList = roomDataSource.getPreferencesForVolunteerSnapshot(volunteerId)

                    // 2. Create sets of composite IDs for comparison
                    val localCompositeIds = localPreferenceList.map { "${it.volunteerId}_${it.homelessId}" }.toSet()
                    val firestoreCompositeIds = firestorePreferencesList.map { "${it.volunteerId}_${it.homelessId}" }.toSet()

                    // 3. Find composite IDs that exist locally but not in Firestore
                    val compositeIdsToDelete = localCompositeIds.minus(firestoreCompositeIds)

                    // 4. Find the actual Preference objects to delete
                    val preferencesToDelete = localPreferenceList.filter {
                        "${it.volunteerId}_${it.homelessId}" in compositeIdsToDelete
                    }

                    // 5. Delete the preferences from Room using the Preference object
                    for (preference in preferencesToDelete) {
                        roomDataSource.deletePreference(preference)
                    }
                }
            }
        }
    }
}
