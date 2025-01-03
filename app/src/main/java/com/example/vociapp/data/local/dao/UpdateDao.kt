package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.types.Area
import kotlinx.coroutines.flow.Flow

@Dao
interface UpdateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(update: Update)

    @Query("SELECT * FROM updates")
    fun getAllUpdates(): Flow<List<Update>>

    @Query("SELECT * FROM updates WHERE homelessId = :homelessId")
    fun getUpdatesByHomelessId(homelessId: String): Flow<List<Update>>

    @Query("SELECT * FROM updates")
    fun getAllUpdatesSnapshot(): List<Update>

    @Query("SELECT * FROM updates WHERE area = :area")
    fun getUpdatesByArea(area: Area): Flow<List<Update>>

    @Transaction
    suspend fun insertOrUpdate(update: Update) {
        val existingUpdate = getUpdateById(update.id)
        if (existingUpdate == null) {
            insert(update)
        } else {
            update(update)
        }
    }

    @Query("SELECT * FROM updates WHERE id = :id LIMIT 1")
    suspend fun getUpdateById(id: String): Update?

    @Query("DELETE FROM updates")
    suspend fun deleteAllUpdates()

    @Query("DELETE FROM updates WHERE id = :updateId")
    suspend fun deleteById(updateId: String)

    @androidx.room.Update
    suspend fun update(update: Update)
}