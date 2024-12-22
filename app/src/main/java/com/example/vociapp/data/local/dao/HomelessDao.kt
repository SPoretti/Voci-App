package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.vociapp.data.local.database.Homeless
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters
interface HomelessDao {
    @Query("SELECT * FROM homelesses WHERE id = :homelessID LIMIT 1")
    suspend fun getHomelessById(homelessID: String): Homeless?

    @Query("SELECT * FROM homelesses")
    fun getAllHomeless(): Flow<List<Homeless>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(homeless: Homeless)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(homelessList: List<Homeless>)
}


