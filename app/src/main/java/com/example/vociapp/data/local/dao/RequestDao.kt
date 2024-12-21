package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vociapp.data.local.database.Request
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {
    @Query("SELECT * FROM requests")
    fun getAllRequests(): Flow<List<Request>>

    @Insert
    suspend fun insert(request: Request)

    @Insert
    suspend fun insertAll(requestsList: List<Request>)
}
