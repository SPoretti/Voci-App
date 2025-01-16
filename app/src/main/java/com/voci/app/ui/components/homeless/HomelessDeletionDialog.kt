package com.voci.app.ui.components.homeless

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.voci.app.data.util.Resource
import com.voci.app.ui.components.core.ConfirmButton
import com.voci.app.ui.components.core.DismissButton
import com.voci.app.ui.viewmodels.HomelessViewModel

@Composable
fun HomelessDeletionDialog(
    homelessViewModel: HomelessViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onClose: () -> Unit
){

    val deletionState by homelessViewModel.deleteHomelessState.collectAsState()

    var showConfirmButton by remember { mutableStateOf(false) }

    var step by remember { mutableIntStateOf(1) }

    AlertDialog(
        // Called when the user tries to dismiss the Dialog by pressing the back button.
        // This is not called when the dismiss button is clicked.
        onDismissRequest = { },
        // Style
        properties = DialogProperties( usePlatformDefaultWidth = false ),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground,
        // Tile
        title = { Text("Eliminazione senzatetto") },
        // Main Content
        text = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                when (step){
                    1 -> {
                        Column {
                            Text("Questa operazione è irreversibile.")
                            Text("Sei sicuro di voler eliminare questo senzatetto?")
                            Text("Ogni richiesta e aggiornamento associati ad esso verranno eliminati.")
                        }
                    }

                    2 ->{
                    when (deletionState) {
                        is Resource.Loading -> {
                            CircularProgressIndicator()
                        }

                        is Resource.Error -> {
                            Text((deletionState as Resource.Error).message ?: "Errore sconosciuto")
                            showConfirmButton = true
                        }

                        is Resource.Success -> {
                            Text("Eliminazione avvenuta con successo")
                            showConfirmButton = true
                        }

                        null -> {}
                    }
                }
                }
            }
        },
        confirmButton = {
            when (step){
                1 -> {
                    ConfirmButton(
                        onClick = {
                            onConfirm()
                            step = 2
                        },
                        text = "Elimina",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        )
                    )
                }

                2 -> {
                    if (showConfirmButton) {
                        DismissButton(
                            onClick = { onClose() },
                            text = "Chiudi"
                        )
                    }
                }
            }
        },
        dismissButton = {
            if (step == 1)
                DismissButton(
                    onClick = { onDismiss() },
                    text = "Annulla"
                )
        }
    )

}