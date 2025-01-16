package com.voci.app.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.voci.app.data.util.Area
import com.voci.app.data.util.Gender
import java.util.UUID

// Homeless entity, used for room database

@Entity(tableName = "homelesses")
data class Homeless(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var gender: Gender = Gender.Unspecified,
    var location: String = "",
    var age: String = "",
    var pets: String = "No",
    var nationality: String = "",
    var description: String = "",
    var status: UpdateStatus = UpdateStatus.GREEN,
    var area: Area = Area.OVEST,
)