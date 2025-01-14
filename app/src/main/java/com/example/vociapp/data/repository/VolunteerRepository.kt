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

    suspend fun getVolunteerIdByEmail(email: String): Resource<String?> {
        return try {
            val volunteerId = roomDataSource.getVolunteerIdByEmail(email)
            if (volunteerId != null)
                Resource.Success(volunteerId)
            else
                Resource.Error("Volontario non trovato, provare a refreshare")
        } catch (e: Exception) {
            Resource.Error("Errore, operazione fallita: ${e.message}")
        }
    }

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

    // Sync actions from the sync queue
    suspend fun syncPendingActions() {
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
}
