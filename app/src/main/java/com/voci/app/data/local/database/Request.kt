package com.voci.app.data.local.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.voci.app.data.util.Area
import com.voci.app.data.util.IconCategory
import java.util.UUID

//Request entity, used for room database, it has two foreign keys: homelessID and creatorId

@Entity(
    tableName = "requests",
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
data class Request(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var creatorId: String? = null,
    var homelessID: String = "",
    var title: String = "",
    var description: String = "",
    var status: RequestStatus = RequestStatus.TODO,
    var timestamp: Long = System.currentTimeMillis(),
    var iconCategory: IconCategory = IconCategory.OTHER,
    var area: Area = Area.OVEST
)

enum class RequestStatus {
    TODO,
    DONE,
}

