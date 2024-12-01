package com.example.vociapp.data.types

data class Homeless(
    val name: String = "",
    val birthDate: String = "",
    val gender: Gender = Gender.Unspecified,
    val location: String = "",
    val needs: List<Request> = emptyList(),
    // ... other properties ...
)