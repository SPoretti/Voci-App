package com.example.vociapp.data.types

import java.util.UUID

data class Homeless(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val birthDate: String = "",
    val gender: Gender = Gender.Unspecified,
    val location: String = "",
)