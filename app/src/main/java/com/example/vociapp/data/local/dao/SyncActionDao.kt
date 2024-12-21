package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.vociapp.data.local.database.SyncAction
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncQueueDao {
    @Insert
    suspend fun addSyncAction(syncAction: SyncAction)

    @Query("SELECT * FROM sync_queue WHERE timestamp <= :timestamp")
    fun getPendingSyncActions(timestamp: Long): Flow<List<SyncAction>>

    @Delete
    suspend fun deleteSyncAction(syncAction: SyncAction)

    @Query("DELETE FROM sync_queue")
    suspend fun clearAllSyncActions()
}
