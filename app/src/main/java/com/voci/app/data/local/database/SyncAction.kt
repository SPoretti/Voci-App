package com.voci.app.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// SyncAction entity, used for room database to store and queue sync actions (performed offline, synced online)

@Entity(tableName = "sync_queue")
data class SyncAction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val entityType: String,        // e.g., "Homeless", "Volunteer"
    val operation: String,         // e.g., "add", "update", "delete"
    val data: String,              // The data to sync, could be serialized object or JSON string
    val timestamp: Long            // Timestamp when the action was queued
)