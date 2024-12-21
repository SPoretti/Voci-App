package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vociapp.data.local.database.Homeless
import kotlinx.coroutines.flow.Flow

@Dao
interface HomelessDao {
    @Query("SELECT * FROM homelesses")
    fun getAllHomeless(): Flow<List<Homeless>>

    @Insert
    suspend fun insert(homeless: Homeless)

    @Insert
    suspend fun insertAll(homelessList: List<Homeless>)
}


