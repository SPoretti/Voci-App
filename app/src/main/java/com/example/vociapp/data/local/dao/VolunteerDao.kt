package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vociapp.data.local.database.Volunteer
import kotlinx.coroutines.flow.Flow

@Dao
interface VolunteerDao {
    @Query("SELECT * FROM volunteers")
    fun getAllVolunteers(): Flow<List<Volunteer>>

    @Insert
    suspend fun insert(volunteer: Volunteer)
}
