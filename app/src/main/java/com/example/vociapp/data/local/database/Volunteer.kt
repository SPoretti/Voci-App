package com.example.vociapp.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vociapp.data.types.Area
import java.util.UUID

//Volunteer entity, used for room database

@Entity(tableName = "volunteers")

data class Volunteer(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var surname: String = "",
    var nickname: String = "",
    var phone_number: String = "",
    var email: String = "",
    var preferredHomelessIds: List<String> = emptyList(),
    var area: Area = Area.OVEST,
)