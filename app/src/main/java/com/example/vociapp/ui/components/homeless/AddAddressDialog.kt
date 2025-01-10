package com.example.vociapp.ui.components.homeless

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.util.Resource

@Composable
fun AddAddressDialog(
    onDismiss: () -> Unit,
    homelessId: String,
    homelessViewModel: HomelessViewModel,
    locationHandler: LocationHandler
) {
    var addressText by remember { mutableStateOf("Loading address...") }
    val homelessState by homelessViewModel.homelesses.collectAsState() // Get the list of homelesses

    LaunchedEffect(key1 = true) {
        locationHandler.getCurrentLocationAddress().onSuccess { address ->
            addressText = address
        }.onFailure { exception ->
            addressText = "Error: ${exception.message}"
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        shape = RoundedCornerShape(0.dp),
        title = { Text("Sei sicuro?") },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "Vuoi aggiornare la posizione attuale del senzatetto?",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text("Indirizzo: $addressText")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Aggiorna la posizione del senzatetto
                    if (homelessState is Resource.Success) {
                        val homelessList = (homelessState as Resource.Success<List<Homeless>>).data
                        if (homelessList != null && homelessList.isNotEmpty()) {
                            // Find the correct homeless by ID
                            val homeless = homelessList.find { it.id == homelessId }
                            if (homeless != null) {
                                homeless.location = addressText
                                homelessViewModel.updateHomeless(homeless)
                            }
                        }
                    }
                    onDismiss()
                }
            ) {
                Text("Aggiorna")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                Text("Annulla")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground,
    )
}