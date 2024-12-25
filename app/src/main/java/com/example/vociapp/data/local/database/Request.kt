package com.example.vociapp.data.local.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.vociapp.ui.components.IconCategory
import java.util.UUID

@Entity(
    tableName = "requests",
    foreignKeys = [
        ForeignKey(
            entity = Homeless::class,
            parentColumns = ["id"],
            childColumns = ["homelessID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Volunteer::class,
            parentColumns = ["id"],
            childColumns = ["creatorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Request(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var creatorId: String? = null,
    var homelessID: String = "",
    var title: String = "",
    var description: String = "",
    var status: RequestStatus = RequestStatus.TODO,
    var timestamp: Long = System.currentTimeMillis(),
    var iconCategory: IconCategory = IconCategory.OTHER
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Request

        if (id != other.id) return false
        if (creatorId != other.creatorId) return false
        if (homelessID != other.homelessID) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (status != other.status) return false
        if (timestamp != other.timestamp) return false
        if (iconCategory != other.iconCategory) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (creatorId?.hashCode() ?: 0)
        result = 31 * result + homelessID.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + iconCategory.hashCode()
        return result
    }
}

enum class RequestStatus {
    TODO,
    DONE,
}

