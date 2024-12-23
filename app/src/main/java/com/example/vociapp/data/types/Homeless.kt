package com.example.vociapp.data.types

import java.util.UUID

data class Homeless(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "Unspecified",
    var gender: Gender = Gender.Unspecified,
    var location: String = "Unknown",
    var age: String = "Unknown",
    var pets: String = "No",
    var nationality: String = "Unknown",
    var description: String = "",
    var status: UpdateStatus = UpdateStatus.GREEN
)