package com.example.vociapp.data.local

import com.example.vociapp.data.local.dao.HomelessDao
import com.example.vociapp.data.local.dao.RequestDao
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.dao.UpdateDao
import com.example.vociapp.data.local.dao.VolunteerDao
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.SyncAction
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

//Offline data source

class RoomDataSource(
    private val homelessDao: HomelessDao,
    private val volunteerDao: VolunteerDao,
    private val requestDao: RequestDao,
    private val updateDao: UpdateDao,
    private val syncQueueDao: SyncQueueDao
) {
    // ------------------------------- Request Functions ----------------------------------

    suspend fun insertRequest(request: Request){
        requestDao.insert(request)
    }

    fun getRequests(): Flow<Resource<List<Request>>>  = flow {
        try {
            // Indicate loading state
            emit(Resource.Loading())
            requestDao.getAllRequests().collect { requestList ->
                // Emit success with the fetched list
                emit(Resource.Success(requestList))
            }
        } catch (e: Exception) {
            //Emit error if there's an issue
            emit(Resource.Error("Error fetching requests from local data: ${e.localizedMessage}"))
        }
    }

    //get all requests from room (not a flow)
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

    fun getRequestsByHomelessId(homelessId: String): Flow<Resource<List<Request>>> = flow {
        try {
            emit(Resource.Loading()) // Indicate loading state
            requestDao.getActiveRequestsByHomelessId(homelessId).collect { requestList ->
                emit(Resource.Success(requestList)) // Emit success with the fetched list
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching requests from local data: ${e.localizedMessage}"))
        }
    }

    // ------------------------------- Homeless Functions ----------------------------------


    suspend fun insertHomeless(homeless: Homeless) {
        homelessDao.insert(homeless)
    }

    // Collecting Flow from Room DAO and emitting Resource
    fun getHomelesses(): Flow<Resource<List<Homeless>>> = flow {
        try {
            // Indicate loading state
            emit(Resource.Loading())
            homelessDao.getAllHomeless().collect { homelessList ->
                // Emit success with the fetched list
                emit(Resource.Success(homelessList))
            }
        } catch (e: Exception) {
            //Emit error if there's an issue
            emit(Resource.Error("Error fetching homeless data: ${e.localizedMessage}")) // Emit error if there's an issue
        }
    }

    //get all homelesses from room (not a flow)
    suspend fun getHomelessesSnapshot(): List<Homeless> = withContext(Dispatchers.IO) {
        homelessDao.getAllHomelessesSnapshot()
    }

    suspend fun getHomelessById(homelessID: String): Homeless?{
        return homelessDao.getHomelessById(homelessID)
    }

    fun getHomelessesLocations():Flow<Resource<List<String>>> = flow {
        try {
            // Indicate loading state
            emit(Resource.Loading())
            homelessDao.getAllLocations().collect { locationsList ->
                // Emit success with the fetched list
                emit(Resource.Success(locationsList))
            }
        } catch (e: Exception) {
            //Emit error if there's an issue
            emit(Resource.Error("Error fetching homeless data: ${e.localizedMessage}")) // Emit error if there's an issue
        }
    }

    suspend fun updateHomeless(homeless: Homeless){
        homelessDao.update(homeless)
    }

    suspend fun insertOrUpdateHomeless(homeless: Homeless) {
        homelessDao.insertOrUpdate(homeless)
    }

    suspend fun deleteHomelessById(homelessID: String) {
        homelessDao.deleteById(homelessID)
    }

    // ------------------------------- Volunteer Functions ----------------------------------

    suspend fun insertVolunteer(volunteer: Volunteer){
        volunteerDao.insert(volunteer)
    }

    suspend fun getVolunteerById(id: String): Volunteer {
        return volunteerDao.getVolunteerById(id)
    }

    suspend fun getVolunteerByNickname(nickname: String): Volunteer? {
        return volunteerDao.getVolunteerByNickname(nickname)
    }

    suspend fun getVolunteerByEmail(email: String): Volunteer {
        return volunteerDao.getVolunteerByEmail(email)
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

    fun getUpdatesByHomelessId(homelessId: String): Flow<Resource<List<Update>>> = flow {
        try {
            emit(Resource.Loading()) // Indicate loading state
            updateDao.getUpdatesByHomelessId(homelessId).collect { updates ->
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

    // ------------------------------- Sync Functions ----------------------------------

    suspend fun isSyncQueueEmpty(): Boolean {
        return syncQueueDao.isEmpty()
    }

    suspend fun addSyncAction(entityType: String, operation: String, data: Any) {
        // Serialize the data object to JSON
        val dataJson = Gson().toJson(data)

        // Create a new sync action to store in the queue
        val syncAction = SyncAction(
            entityType = entityType,
            operation = operation,
            data = dataJson,
            timestamp = System.currentTimeMillis()
        )

        // Add to the sync queue
        syncQueueDao.addSyncAction(syncAction)
    }

    suspend fun deleteSyncAction(syncAction: SyncAction) {
        syncQueueDao.deleteSyncAction(syncAction)
    }

    fun getPendingSyncActions(timestamp: Long): Flow<List<SyncAction>> {
        return syncQueueDao.getPendingSyncActions(timestamp)
    }
}
