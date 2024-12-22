package com.example.vociapp.data.local

import com.example.vociapp.data.local.dao.HomelessDao
import com.example.vociapp.data.local.dao.RequestDao
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.dao.VolunteerDao
import com.example.vociapp.data.local.database.Converters
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomDataSource(
    val homelessDao: HomelessDao,
    private val volunteerDao: VolunteerDao,
    private val requestDao: RequestDao,
    private val syncQueueDao: SyncQueueDao
) {

    suspend fun insertHomeless(homeless: Homeless) {
        homelessDao.insert(homeless)
    }

    suspend fun insertVolunteer(volunteer: Volunteer): Resource<String> {
        return try {
            volunteerDao.insert(volunteer)
            Resource.Success("Volunteer added successfully")
        } catch (e: Exception) {
            Resource.Error("Error inserting volunteer: ${e.localizedMessage}")
        }
    }

    suspend fun insertRequest(request: Request): Resource<String>{
        return try {
            requestDao.insert(request)
            Resource.Success("Request added successfully")
        } catch (e: Exception) {
            Resource.Error("Error inserting request: ${e.localizedMessage}")
        }
    }

    // Retrieve data

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

    fun getVolunteers(): Flow<List<Volunteer>> {
        return volunteerDao.getAllVolunteers()
    }

    suspend fun getVolunteerById(id: String): Volunteer? {
        return volunteerDao.getVolunteerById(id)
    }

    suspend fun getVolunteerByEmail(email: String): Volunteer? {
        return volunteerDao.getVolunteerByEmail(email)
    }

    suspend fun getVolunteerIdByEmail(email: String): String? {
        return volunteerDao.getVolunteerIdByEmail(email)
    }

    suspend fun getVolunteerByNickname(nickname: String): Volunteer? {
        return volunteerDao.getVolunteerByNickname(nickname)
    }

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
            // Check if user exists in local database
//            val volunteer = volunteerDao.getVolunteerById(userId) // Fetch the volunteer from Room database

//            if (volunteer != null) {
                // If volunteer exists, update their preferences locally
                volunteerDao.updateUserPreferences(userId, preferredHomelessIds) // Assuming this DAO function exists
                Resource.Success(Unit) // Successfully updated
//            } else {
//                Resource.Error("Volunteer not found") // Handle case where volunteer is not found
//            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred") // Handle any exceptions
        }
    }


    fun getRequests(): Flow<List<Request>> {
        return requestDao.getAllRequests()
    }

    //Update data
    // Update volunteer
    suspend fun updateVolunteer(volunteer: Volunteer): Resource<Unit> {
        return try {
            volunteerDao.update(volunteer)
            Resource.Success(Unit)  // Return success without any data
        } catch (e: Exception) {
            Resource.Error("Error updating volunteer: ${e.localizedMessage}")
        }
    }
}
