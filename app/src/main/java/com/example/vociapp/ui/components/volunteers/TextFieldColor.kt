package com.example.vociapp.ui.components.volunteers

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable

@Composable
fun getTextFieldColors(isValid: Boolean): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = if (isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        unfocusedBorderColor = if (isValid) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f) else MaterialTheme.colorScheme.error,
        cursorColor = if (isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        focusedLabelColor = if (isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        unfocusedLabelColor = if (isValid) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
    )
}