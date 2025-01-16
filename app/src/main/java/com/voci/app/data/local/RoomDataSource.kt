package com.voci.app.data.local

import com.google.gson.Gson
import com.voci.app.data.local.dao.HomelessDao
import com.voci.app.data.local.dao.PreferenceDao
import com.voci.app.data.local.dao.RequestDao
import com.voci.app.data.local.dao.SyncQueueDao
import com.voci.app.data.local.dao.UpdateDao
import com.voci.app.data.local.dao.VolunteerDao
import com.voci.app.data.local.database.Homeless
import com.voci.app.data.local.database.Preference
import com.voci.app.data.local.database.Request
import com.voci.app.data.local.database.SyncAction
import com.voci.app.data.local.database.Update
import com.voci.app.data.local.database.Volunteer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

//Offline data source

class RoomDataSource(
    private val homelessDao: HomelessDao,
    private val volunteerDao: VolunteerDao,
    private val preferenceDao: PreferenceDao,
    private val requestDao: RequestDao,
    private val updateDao: UpdateDao,
    private val syncQueueDao: SyncQueueDao
) {
    // ------------------------------- Request Functions ----------------------------------

    suspend fun insertRequest(request: Request){
        requestDao.insert(request)
    }

    fun getRequests(): Flow<List<Request>> {
        return requestDao.getAllRequests()
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

    fun getRequestsByHomelessId(homelessId: String): Flow<List<Request>> {
        return requestDao.getActiveRequestsByHomelessId(homelessId)
    }

    fun getActiveRequests() : Flow<List<Request>> {
        return requestDao.getActiveRequests()
    }

    fun getCompletedRequests() : Flow<List<Request>> {
        return requestDao.getCompletedRequests()
    }

    // ------------------------------- Homeless Functions ----------------------------------


    suspend fun insertHomeless(homeless: Homeless) {
        homelessDao.insert(homeless)
    }

    // Collecting Flow from Room DAO and emitting Resource
    fun getHomelesses(): Flow<List<Homeless>> {
        return homelessDao.getAllHomelesses()
    }

    //get all homelesses from room (not a flow)
    suspend fun getHomelessesSnapshot(): List<Homeless> = withContext(Dispatchers.IO) {
        homelessDao.getAllHomelessesSnapshot()
    }

    suspend fun getHomelessById(homelessID: String): Homeless?{
        return homelessDao.getHomelessById(homelessID)
    }

    fun getHomelessesLocations():Flow<List<String>> {
        return homelessDao.getAllLocations()
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

    suspend fun getVolunteerById(id: String): Volunteer? {
        return volunteerDao.getVolunteerById(id)
    }

    suspend fun getVolunteerByNickname(nickname: String): Volunteer? {
        return volunteerDao.getVolunteerByNickname(nickname)
    }

    suspend fun getVolunteerByEmail(email: String): Volunteer? {
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

    fun getUpdates(): Flow<List<Update>> {
        return updateDao.getAllUpdates()
    }

    fun getUpdatesByHomelessId(homelessId: String): Flow<List<Update>> {
        return updateDao.getUpdatesByHomelessId(homelessId)
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
    // ------------------------------- Preferences Functions ----------------------------------
    suspend fun insertPreference(preference: Preference) {
        preferenceDao.insert(preference)
    }

    suspend fun deletePreference(preference: Preference) {
        preferenceDao.delete(preference)
    }

    fun getPreferencesForVolunteer(volunteerId: String): Flow<List<Preference>> {
        return preferenceDao.getPreferencesForVolunteer(volunteerId)
    }

    suspend fun getPreferencesForVolunteerSnapshot(volunteerId: String): List<Preference> = withContext(Dispatchers.IO) {
        preferenceDao.getPreferencesForVolunteerSnapshot(volunteerId)
    }
}
