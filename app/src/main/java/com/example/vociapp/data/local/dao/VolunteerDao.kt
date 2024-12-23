package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.vociapp.data.local.database.Converters
import com.example.vociapp.data.local.database.Volunteer
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(Converters::class)
interface VolunteerDao {
    @Query("SELECT * FROM volunteers")
    fun getAllVolunteers(): Flow<List<Volunteer>>

    @Query("SELECT * FROM volunteers WHERE id = :id LIMIT 1")
    suspend fun getVolunteerById(id: String): Volunteer?

    @Query("SELECT * FROM volunteers WHERE email = :email LIMIT 1")
    suspend fun getVolunteerByEmail(email: String): Volunteer?

    @Query("SELECT id FROM volunteers WHERE email = :email LIMIT 1")
    suspend fun getVolunteerIdByEmail(email: String): String?

    @Query("SELECT * FROM volunteers WHERE nickname = :nickname LIMIT 1")
    suspend fun getVolunteerByNickname(nickname: String): Volunteer?

    @Query("SELECT preferredHomelessIds FROM volunteers WHERE id = :userId")
    suspend fun getUserPreferences(userId: String): String?

    @Query("UPDATE volunteers SET preferredHomelessIds = :preferredHomelessIds WHERE id = :userId")
    suspend fun updateUserPreferences(userId: String, preferredHomelessIds: List<String>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(volunteer: Volunteer)

    @Update
    suspend fun update(volunteer: Volunteer)
}
