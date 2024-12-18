package com.example.vociapp.data.types

import java.util.UUID

data class Request(
    val id: String = UUID.randomUUID().toString(),
    val creatorId: String? = null,
    val homelessID: String = "",
    val title: String = "",
    val description: String = "",
    var status: RequestStatus = RequestStatus.TODO,
    val timestamp: Long = System.currentTimeMillis()
)

enum class RequestStatus {
    TODO,
    DONE,
}