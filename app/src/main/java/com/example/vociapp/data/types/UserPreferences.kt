package com.example.vociapp.data.types

data class UserPreferences(
    val userId: String = "",
    val preferredHomelessIds: List<String> = emptyList()
)