package com.voci.app.ui.components.volunteers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

@Composable
fun PasswordPopup(onDismiss: () -> Unit) {
    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, -150),
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .clickable { onDismiss() }
        ) {
            Text(
                text = """
                            - Almeno 8 caratteri
                            - Almeno una lettera maiuscola
                            - Almeno una lettera minuscola
                            - Almeno un numero
                            - Almeno un carattere speciale (@$!#%*?&)
                        """.trimIndent(),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}