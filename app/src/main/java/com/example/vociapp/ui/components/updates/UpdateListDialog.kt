package com.example.vociapp.ui.components.updates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.util.Resource
import com.example.vociapp.ui.components.core.ConfirmButton
import com.example.vociapp.ui.components.core.DismissButton

@Composable
fun UpdateListDialog(
    onDismiss: () -> Unit,
    updates: Resource<List<Update>>,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        shape = RoundedCornerShape(0.dp),
        title = { Text("Lista Aggiornamenti") },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                UpdateList(updates)
            }
        },
        dismissButton = {
            DismissButton(
                onClick = { onDismiss() },
                text = "Chiudi"
            )
        },
        confirmButton = {
            ConfirmButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                text = "Aggiungi"
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground,
    )
}