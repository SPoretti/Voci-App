package com.example.vociapp.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "volunteers")
data class Volunteer(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var surname: String = "",
    var nickname: String = "",
    var password: String = "",
    var phone_number: String = "",
    var email: String = "",
    var preferredHomelessIds: List<String> = emptyList(),
)