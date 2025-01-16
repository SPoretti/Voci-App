package com.voci.app.ui.components.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DismissButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ),
    enabled: Boolean = true,
    text: String = "Annulla",
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
){
    OutlinedButton(
        onClick = { onClick() },
        colors = colors,
        border = border,
        modifier = modifier.widthIn(min = 90.dp),
        enabled = enabled
    ) {
        Text(text)
    }
}