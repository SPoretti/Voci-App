package com.example.vociapp.data.local

import com.example.vociapp.data.local.dao.HomelessDao
import com.example.vociapp.data.local.dao.RequestDao
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.dao.VolunteerDao
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.util.Resource
import kotlinx.coroutines.flow.Flow

class RoomDataSource(
    private val homelessDao: HomelessDao,
    private val volunteerDao: VolunteerDao,
    private val requestDao: RequestDao,
    private val syncQueueDao: SyncQueueDao
) {

    suspend fun insertHomeless(homeless: Homeless) {
        homelessDao.insert(homeless)
    }

    suspend fun insertVolunteer(volunteer: Volunteer): Resource<String> {
        return try {
            volunteerDao.insertVolunteer(volunteer)
            Resource.Success("Volunteer added successfully")
        } catch (e: Exception) {
            Resource.Error("Error inserting volunteer: ${e.localizedMessage}")
        }
    }

    suspend fun insertRequest(request: Request) {
        requestDao.insert(request)
    }

    // Retrieve data
    fun getHomelesses(): Flow<List<Homeless>> {
        return homelessDao.getAllHomeless()
    }

    fun getVolunteers(): Flow<List<Volunteer>> {
        return volunteerDao.getAllVolunteers()
    }

    fun getRequests(): Flow<List<Request>> {
        return requestDao.getAllRequests()
    }

    //Update data
    // Update volunteer
    suspend fun updateVolunteer(volunteer: Volunteer): Resource<Unit> {
        return try {
            volunteerDao.updateVolunteer(volunteer)
            Resource.Success(Unit)  // Return success without any data
        } catch (e: Exception) {
            Resource.Error("Error updating volunteer: ${e.localizedMessage}")
        }
    }
}
