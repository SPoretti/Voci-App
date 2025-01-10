package com.example.vociapp.data.local.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.vociapp.data.util.Area
import java.util.UUID

// Update entity, used for room database, it has two foreign keys: homelessID and creatorId

@Entity(
    tableName = "updates",
    foreignKeys = [
        ForeignKey(
            entity = Homeless::class,
            parentColumns = ["id"],
            childColumns = ["homelessID"],
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Volunteer::class,
            parentColumns = ["id"],
            childColumns = ["creatorId"],
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.SET_DEFAULT
        )
    ]
)
data class Update(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var creatorId: String = "",
    var homelessID: String = "",
    var title: String = "",
    var description: String = "",
    var status: UpdateStatus = UpdateStatus.GREEN,
    var timestamp: Long = System.currentTimeMillis(),
    var area: Area = Area.OVEST,
)

enum class UpdateStatus {
    GREEN,
    YELLOW,
    RED,
    GRAY
}