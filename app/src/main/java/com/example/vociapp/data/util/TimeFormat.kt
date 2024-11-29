package com.example.vociapp.data.util

import java.text.SimpleDateFormat
import java.util.Locale

interface DateTimeFormatter {
    fun formatTimestamp(timestamp: Long): String
    fun formatDate(timestamp: Long): String
}

class DateTimeFormatterImpl : DateTimeFormatter {
    override fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(timestamp)
    }

    override fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(timestamp)
    }
}