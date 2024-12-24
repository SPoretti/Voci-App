package com.example.vociapp.data.types

import java.util.UUID

data class Update(
    val id: String = UUID.randomUUID().toString(),
    val creatorId: String? = null,
    var homelessID: String = "",
    var title: String = "",
    var description: String = "",
    var status: UpdateStatus = UpdateStatus.GREEN,
    val timestamp: Long = System.currentTimeMillis()
)

enum class UpdateStatus {
    GREEN,
    YELLOW,
    RED,
    GRAY
}