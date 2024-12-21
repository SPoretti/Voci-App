package com.example.vociapp.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vociapp.data.types.Gender
import java.util.UUID

@Entity(tableName = "homelesses")
data class Homeless(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var gender: Gender = Gender.Unspecified,
    var location: String = "",
    var age: String = "",
    var pets: String = "No",
    var nationality: String = "",
    var description: String = "",
)