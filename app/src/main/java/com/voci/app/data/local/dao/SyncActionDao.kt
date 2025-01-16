package com.voci.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.voci.app.data.local.database.SyncAction
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncQueueDao {
    @Insert
    suspend fun addSyncAction(syncAction: SyncAction)

    @Delete
    suspend fun deleteSyncAction(syncAction: SyncAction)

    @Query("SELECT * FROM sync_queue WHERE timestamp <= :timestamp ORDER BY timestamp ASC")
    fun getPendingSyncActions(timestamp: Long): Flow<List<SyncAction>>

    @Query("DELETE FROM sync_queue")
    suspend fun clearAllSyncActions()

    @Query("SELECT COUNT(*) FROM sync_queue")
    suspend fun getRowCount(): Int

    suspend fun isEmpty(): Boolean {
        return getRowCount() == 0
    }
}
