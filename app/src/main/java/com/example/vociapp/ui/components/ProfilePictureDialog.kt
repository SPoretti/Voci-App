package com.example.vociapp.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ProfilePictureDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    initialPhotoUrl : String
) {
    var photoUrl by remember { mutableStateOf(initialPhotoUrl) }
    var isConfirmed by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Inserisci un URL valido",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // ProfileTextField per l'immagine del profilo
                    ProfileTextField(
                        value = photoUrl,
                        onValueChange = { photoUrl = it },
                        label = "Immagine Profilo",
                        icon = Icons.Default.Face,
                        placeholder = "URL Immagine Profilo",
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            isConfirmed = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Conferma")
                    }

                    LaunchedEffect(isConfirmed) {
                        if (isConfirmed) {
                            onSave(photoUrl)
                            onDismiss()
                            isConfirmed = false
                        }
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Chiudi")
                    }
                }
            }
        }
    }
}
