package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.util.Area
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters
interface HomelessDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(homeless: Homeless)

    @Update
    suspend fun update(homeless: Homeless)

    @Update
    suspend fun updateAll(homelessList: List<Homeless>)

    @Query("DELETE FROM homelesses WHERE id = :homelessID")
    suspend fun deleteById(homelessID: String)

    @Query("SELECT * FROM homelesses")
    fun getAllHomelesses(): Flow<List<Homeless>>

    @Query("SELECT * FROM homelesses")
    suspend fun getAllHomelessesSnapshot(): List<Homeless>

    @Query("SELECT * FROM homelesses WHERE id = :homelessID LIMIT 1")
    suspend fun getHomelessById(homelessID: String): Homeless?

    @Query("SELECT * FROM homelesses WHERE area = :area")
    fun getHomelessesByArea(area: Area): Flow<List<Homeless>>

    @Query("SELECT location FROM homelesses")
    fun getAllLocations(): Flow<List<String>>

    @Transaction
    suspend fun insertOrUpdate(homeless: Homeless) {
        val existingHomeless = getHomelessById(homeless.id)
        if (existingHomeless == null) {
            insert(homeless)
        } else {
            update(homeless)
        }
    }
}


