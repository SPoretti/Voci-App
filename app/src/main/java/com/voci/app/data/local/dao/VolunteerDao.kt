package com.voci.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import androidx.room.Update
import com.voci.app.data.local.database.Converters
import com.voci.app.data.local.database.Volunteer
import com.voci.app.data.util.Area
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(Converters::class)
interface VolunteerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(volunteer: Volunteer)

    @Update
    suspend fun update(volunteer: Volunteer)

    @Query("DELETE FROM volunteers WHERE id = :volunteerId")
    suspend fun deleteById(volunteerId: String)

    @Query("SELECT * FROM volunteers")
    fun getAllVolunteers(): Flow<List<Volunteer>>

    @Query("SELECT * FROM volunteers")
    suspend fun getAllVolunteersSnapshot(): List<Volunteer>

    @Query("SELECT * FROM volunteers WHERE id = :id LIMIT 1")
    suspend fun getVolunteerById(id: String): Volunteer?

    @Query("SELECT * FROM volunteers WHERE email = :email LIMIT 1")
    suspend fun getVolunteerByEmail(email: String): Volunteer?

    @Query("SELECT id FROM volunteers WHERE email = :email LIMIT 1")
    suspend fun getVolunteerIdByEmail(email: String): String?

    @Query("SELECT * FROM volunteers WHERE nickname = :nickname LIMIT 1")
    suspend fun getVolunteerByNickname(nickname: String): Volunteer?

    @Query("SELECT * FROM volunteers WHERE area = :area")
    fun getVolunteersByArea(area: Area): Flow<List<Volunteer>>

    @Transaction
    suspend fun insertOrUpdate(volunteer: Volunteer) {
        val existingVolunteer = getVolunteerById(volunteer.id)
        if (existingVolunteer == null) {
            insert(volunteer)
        } else {
            update(volunteer)
        }
    }
}
