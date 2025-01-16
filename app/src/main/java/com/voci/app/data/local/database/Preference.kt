package com.voci.app.data.local.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "preferences",
    primaryKeys = ["volunteerId", "homelessId"],
    foreignKeys = [
        ForeignKey(
            entity = Volunteer::class,
            parentColumns = ["id"],
            childColumns = ["volunteerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Homeless::class,
            parentColumns = ["id"],
            childColumns = ["homelessId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Preference(
    val volunteerId: String = "",
    val homelessId: String = ""
)