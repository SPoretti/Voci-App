package com.voci.app.ui.components.maps

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.voci.app.ui.components.core.ConfirmButton
import com.voci.app.ui.components.core.DismissButton

@Composable
fun LocationSelectionDialog(
    location: String,
    onConfirmLocation: (String) -> Unit,
    onDismiss: () -> Unit,
){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Vuoi selezionare la seguente posizione?") },
        text = {
            if (location.isNotEmpty())
                Text(text = location)
            else
                Text(
                    text = "Nessuna posizione selezionata",
                    color = MaterialTheme.colorScheme.error
                )
               },
        confirmButton = {
            ConfirmButton(
                onClick = {
                    onConfirmLocation(location)
                    onDismiss()
                },
                enabled = location.isNotEmpty(),
            )
        },
        dismissButton = {
            DismissButton(onClick = { onDismiss() })
        }
    )
}