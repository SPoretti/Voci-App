package com.example.vociapp.data.model

import java.util.UUID

data class Request(
    val id: String = UUID.randomUUID().toString(),
    val userId: String? = null,
    val homelessID: String? = null,
    val title: String = "",
    val description: String = "",
    val status: RequestStatus = RequestStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis()
)

enum class RequestStatus {
    PENDING,
    APPROVED,
    REJECTED
}