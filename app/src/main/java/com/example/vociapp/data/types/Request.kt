package com.example.vociapp.data.types

import java.util.UUID

data class Request(
    val id: String = UUID.randomUUID().toString(),
    val creatorId: String? = null,
    val homelessID: String? = null,
    val title: String = "",
    val description: String = "",
    val status: RequestStatus = RequestStatus.TODO,
    val timestamp: Long = System.currentTimeMillis()
)

enum class RequestStatus {
    TODO,
    DONE,
}