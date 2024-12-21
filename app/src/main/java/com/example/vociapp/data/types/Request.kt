package com.example.vociapp.data.types

import com.example.vociapp.ui.components.IconCategory
import java.util.UUID

data class Request(
    val id: String = UUID.randomUUID().toString(),
    val creatorId: String? = null,
    var homelessID: String = "",
    var title: String = "",
    var description: String = "",
    var status: RequestStatus = RequestStatus.TODO,
    val timestamp: Long = System.currentTimeMillis(),
    var iconCategory: IconCategory = IconCategory.OTHER
)

enum class RequestStatus {
    TODO,
    DONE,
}