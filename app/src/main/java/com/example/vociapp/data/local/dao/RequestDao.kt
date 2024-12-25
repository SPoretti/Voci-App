package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vociapp.data.local.database.Request
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {
    @Query("SELECT * FROM requests")
    fun getAllRequests(): Flow<List<Request>>

    @Query("SELECT * FROM requests WHERE id = :requestId LIMIT 1")
    fun getRequestById(requestId: String): Request

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(request: Request)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(requestsList: List<Request>)

    @Update
    fun update(request: Request)

    @Delete
    fun delete(request: Request)
}
