package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vociapp.data.local.database.Preference
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferenceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(preference: Preference)

    @Delete
    suspend fun delete(preference: Preference)

    @Query("SELECT * FROM preferences WHERE volunteerId = :volunteerId")
    fun getPreferencesForVolunteer(volunteerId: String): Flow<List<Preference>>

    @Query("SELECT * FROM preferences WHERE volunteerId = :volunteerId")
    suspend fun getPreferencesForVolunteerSnapshot(volunteerId: String): List<Preference>
}