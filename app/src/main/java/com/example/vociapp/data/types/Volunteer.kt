package com.example.vociapp.data.types

import java.util.UUID

data class Volunteer(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var surname: String = "",
    var nickname: String = "",
    var password: String = "",
    var phone_number: String = "",
    var email: String = "",
    //val preferences: List<String> = emptyList(),
    // ... other properties ...
)