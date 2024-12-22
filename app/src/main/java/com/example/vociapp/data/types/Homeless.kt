package com.example.vociapp.data.types

import java.util.UUID

data class Homeless(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var gender: Gender = Gender.Unspecified,
    var location: String = "",
    var age: String = "",
    var pets: String = "No",
    var nationality: String = "",
    var description: String = "",
    var status: UpdateStatus = UpdateStatus.GREEN
)