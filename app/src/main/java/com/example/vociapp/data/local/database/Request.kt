package com.example.vociapp.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vociapp.ui.components.IconCategory
import java.util.UUID

@Entity(tableName = "requests")
data class Request(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var creatorId: String? = null,
    var homelessID: String = "",
    var title: String = "",
    var description: String = "",
    var status: RequestStatus = RequestStatus.TODO,
    var timestamp: Long = System.currentTimeMillis(),
    var iconCategory: IconCategory = IconCategory.OTHER
)

enum class RequestStatus {
    TODO,
    DONE,
}