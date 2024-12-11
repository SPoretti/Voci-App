package com.example.vociapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight


@Composable
fun CustomSnackbar(snackbarData: SnackbarData) {
    val isKeyboardOpen by keyboardAsState()

    val snackbarModifier = Modifier
        .let {
            if (isKeyboardOpen) {
                it.offset(y = (-600).dp)
            } else {
                it
            }
        }
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.primary)
        .size(280.dp, 60.dp)
        .padding(16.dp)

    Box(
        modifier = snackbarModifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = snackbarData.visuals.message,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

