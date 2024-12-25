package com.example.vociapp.data.local

import com.example.vociapp.data.local.dao.HomelessDao
import com.example.vociapp.data.local.dao.RequestDao
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.dao.VolunteerDao
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomDataSource(
    val homelessDao: HomelessDao,
    private val volunteerDao: VolunteerDao,
    private val requestDao: RequestDao,
    val syncQueueDao: SyncQueueDao
) {
    // ------------------------------- Request Functions ----------------------------------

    suspend fun insertRequest(request: Request): Resource<String>{
        return try {
            requestDao.insert(request)
            Resource.Success("Richiesta aggiunta con successo")
        } catch (e: Exception) {
            Resource.Error("Error inserting request: ${e.localizedMessage}")
        }
    }

    fun getRequests(): Flow<Resource<List<Request>>>  = flow {
        try {
            emit(Resource.Loading()) // Indicate loading state
            requestDao.getAllRequests().collect { requestList ->
                emit(Resource.Success(requestList)) // Emit success with the fetched list
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching requests from local data: ${e.localizedMessage}"))
        }
    }

    fun updateRequest(request: Request):Resource<Unit> {
        return try {
            Resource.Success(requestDao.update(request))
        } catch (e: Exception) {
            Resource.Error("Error updating request: ${e.localizedMessage}")
        }
    }

    fun deleteRequest(request: Request): Resource<Unit> {
        return try{
            Resource.Success(requestDao.delete(request))
        }catch (e: Exception) {
            Resource.Error("Error deleting request: ${e.localizedMessage}")
        }
    }

    fun getRequestById(requestId: String): Resource<Request> {
        return try{
            Resource.Success(requestDao.getRequestById(requestId))
        } catch (e: Exception) {
            Resource.Error("Error getting request by ID: ${e.localizedMessage}")
        }
    }

    // ------------------------------- Homeless Functions ----------------------------------


    suspend fun insertHomeless(homeless: Homeless) {
        homelessDao.insert(homeless)
    }

    // Collecting Flow from Room DAO and emitting Resource
    fun getHomelesses(): Flow<Resource<List<Homeless>>> = flow {
        try {
            emit(Resource.Loading()) // Indicate loading state
            homelessDao.getAllHomeless().collect { homelessList ->
                emit(Resource.Success(homelessList)) // Emit success with the fetched list
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching homeless data: ${e.localizedMessage}")) // Emit error if there's an issue
        }
    }

    suspend fun getHomeless(homelessID: String): Resource<Homeless>{
        val homeless = homelessDao.getHomelessById(homelessID)
        if (homeless != null)
            return Resource.Success(homeless)
        else
            return Resource.Error("Homeless not found")
    }

    suspend fun updateHomeless(homeless: Homeless): Resource<Unit> {
        return try{
            Resource.Success(homelessDao.update(homeless))
        } catch (e: Exception){
            Resource.Error("Errore durante l'aggiornamento del profilo: ${e.localizedMessage}")
        }
    }

    suspend fun deleteHomeless(homelessID: String) {
        homelessDao.deleteById(homelessID)
    }

    //----------------------------------TODO()-------------------------------
    // ------------------------------- Volunteer Functions ----------------------------------

    suspend fun insertVolunteer(volunteer: Volunteer): Resource<String> {
        return try {
            volunteerDao.insert(volunteer)
            Resource.Success("Volunteer added successfully")
        } catch (e: Exception) {
            Resource.Error("Error inserting volunteer: ${e.localizedMessage}")
        }
    }

    suspend fun getVolunteerById(id: String): Volunteer? {
        return volunteerDao.getVolunteerById(id)
    }

    suspend fun getVolunteerByNickname(nickname: String): Volunteer? {
        return volunteerDao.getVolunteerByNickname(nickname)
    }

    suspend fun getVolunteerByEmail(email: String): Volunteer? {
        return volunteerDao.getVolunteerByEmail(email)
    }

    suspend fun getVolunteerIdByEmail(email: String): String? {
        return volunteerDao.getVolunteerIdByEmail(email)
    }

    suspend fun updateVolunteer(volunteer: Volunteer): Resource<Unit> {
        return try {
            volunteerDao.update(volunteer)
            Resource.Success(Unit)  // Return success without any data
        } catch (e: Exception) {
            Resource.Error("Error updating volunteer: ${e.localizedMessage}")
        }
    }

    fun getVolunteers(): Flow<List<Volunteer>> {
        return volunteerDao.getAllVolunteers()
    }

    // ------------------------------- Preferences Functions ----------------------------------

    suspend fun getUserPreferences(userId: String): Resource<String> {
        return try {
            // Query the local database for the volunteer's preferences
            val preferences = volunteerDao.getUserPreferences(userId)
            if (preferences != null) {
                Resource.Success(preferences)
            } else {
                Resource.Success("") // Return an empty json string if preferences are not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred while retrieving user preferences")
        }
    }

    suspend fun updateUserPreferences(userId: String, preferredHomelessIds: List<String>): Resource<Unit> {
        return try {
            volunteerDao.updateUserPreferences(userId, preferredHomelessIds) // Assuming this DAO function exists
            Resource.Success(Unit)
        }
        catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred") // Handle any exceptions
        }
    }

    // ------------------------------- Updates Functions ----------------------------------

    suspend fun insertUpdate(update: Update): Resource<String> {
        TODO()
    }

    suspend fun getUpdates(): Resource<List<Update>> {
        TODO()
    }
}
