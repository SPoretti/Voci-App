package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vociapp.data.local.database.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UpdateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdate(update: Update): Long

    @Query("SELECT * FROM updates")
    fun getUpdates(): Flow<List<Update>>

    @Query("DELETE FROM updates")
    suspend fun deleteAllUpdates()
}