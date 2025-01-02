package com.example.vociapp.data.local

import com.example.vociapp.data.local.dao.HomelessDao
import com.example.vociapp.data.local.dao.RequestDao
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.dao.UpdateDao
import com.example.vociapp.data.local.dao.VolunteerDao
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class RoomDataSource(
    val homelessDao: HomelessDao,
    private val volunteerDao: VolunteerDao,
    val requestDao: RequestDao,
    val updateDao: UpdateDao,
    val syncQueueDao: SyncQueueDao
) {
    // ------------------------------- Request Functions ----------------------------------

    suspend fun insertRequest(request: Request){
        requestDao.insert(request)
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

    suspend fun getRequestsSnapshot(): List<Request> = withContext(Dispatchers.IO) {
        requestDao.getAllRequestsSnapshot()
    }

    suspend fun insertOrUpdateRequest(request: Request) {
        requestDao.insertOrUpdate(request)
    }

    suspend fun updateRequest(request: Request) {
        requestDao.update(request)
    }

    suspend fun deleteRequest(request: Request){
        requestDao.delete(request)
    }

    suspend fun deleteRequestById(requestId: String) {
        requestDao.deleteById(requestId)
    }

    suspend fun getRequestById(requestId: String): Request {
        return requestDao.getRequestById(requestId)
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

    suspend fun getHomelessesSnapshot(): List<Homeless> = withContext(Dispatchers.IO) {
        homelessDao.getAllHomelessesSnapshot()
    }

    suspend fun getHomelessById(homelessID: String): Homeless?{
        return homelessDao.getHomelessById(homelessID)
    }

    suspend fun updateHomeless(homeless: Homeless){
        homelessDao.update(homeless)
    }

    suspend fun insertOrUpdateHomeless(homeless: Homeless) {
        homelessDao.insertOrUpdate(homeless)
    }

    suspend fun deleteHomeless(homelessID: String) {
        homelessDao.deleteById(homelessID)
    }

    // ------------------------------- Volunteer Functions ----------------------------------

    suspend fun insertVolunteer(volunteer: Volunteer){
        volunteerDao.insert(volunteer)
    }

    suspend fun getVolunteerById(id: String): Volunteer? {
        return volunteerDao.getVolunteerById(id)
    }

    suspend fun getVolunteerByNickname(nickname: String): Volunteer? {
        return volunteerDao.getVolunteerByNickname(nickname)
    }

    suspend fun getVolunteerByEmail(email: String): Volunteer {
        return volunteerDao.getVolunteerByEmail(email)
    }

    suspend fun getVolunteerIdByEmail(email: String): String? {
        return volunteerDao.getVolunteerIdByEmail(email)
    }

    suspend fun updateVolunteer(volunteer: Volunteer){
        volunteerDao.update(volunteer)
    }

    fun getVolunteers(): Flow<List<Volunteer>> {
        return volunteerDao.getAllVolunteers()
    }

    suspend fun getVolunteersSnapshot(): List<Volunteer> {
        return volunteerDao.getAllVolunteersSnapshot()
    }

    suspend fun insertOrUpdateVolunteer(volunteer: Volunteer) {
        volunteerDao.insertOrUpdate(volunteer)
    }

    suspend fun deleteVolunteerById(volunteerId: String) {
        volunteerDao.deleteById(volunteerId)
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

    suspend fun updateUserPreferences(userId: String, preferredHomelessIds: List<String>) {
        volunteerDao.updateUserPreferences(userId, preferredHomelessIds)
    }

    // ------------------------------- Updates Functions ----------------------------------

    suspend fun insertUpdate(update: Update) {
        updateDao.insert(update)
    }

    fun getUpdates(): Flow<Resource<List<Update>>> = flow {
        try {
            emit(Resource.Loading()) // Indicate loading state
            updateDao.getAllUpdates().collect { updates ->
                emit(Resource.Success(updates)) // Emit success with the fetched list
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching updates: ${e.message}")) // Emit error if there's an issue
        }
    }

    suspend fun getUpdatesSnapshot(): List<Update> = withContext(Dispatchers.IO) {
        updateDao.getAllUpdatesSnapshot()
    }

    suspend fun insertOrUpdateUpdate(update: Update) {
        updateDao.insertOrUpdate(update)
    }

    suspend fun deleteUpdateById(updateId: String) {
        updateDao.deleteById(updateId)
    }




}
