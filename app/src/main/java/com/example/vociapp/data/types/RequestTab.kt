package com.example.vociapp.data.types

sealed class RequestTab(val title: String, val status: RequestStatus) {
    object ToDo : RequestTab("To Do", RequestStatus.TODO)
    object Done : RequestTab("Done", RequestStatus.DONE)
}