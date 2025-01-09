package com.example.vociapp.ui.components

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter

@Composable
fun ProfilePictureDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    initialPhotoUrl : String
) {
    var photoUrl by remember { mutableStateOf(initialPhotoUrl) }
    var isConfirmed by remember { mutableStateOf(false) }
    var isUrlValid by remember { mutableStateOf(true) }

    fun isValidUrl(url: String): Boolean {
        return if (url.isEmpty()){
            true
        } else {
            Patterns.WEB_URL.matcher(url).matches()
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!isUrlValid) {
                        Text(
                            "L'URL inserito non è valido",
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            "Inserisci un URL valido",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // ProfileTextField per l'immagine del profilo
                    ProfileTextField(
                        value = photoUrl,
                        onValueChange = { photoUrl = it },
                        label = "Immagine Profilo",
                        placeholder = "URL Immagine Profilo",
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true
                    )

                    // Visualizzazione anteprima immagine sopra il ProfileTextField
                    if (isUrlValid && photoUrl.isNotEmpty()) {
                        val painter = rememberAsyncImagePainter(photoUrl)
                        Image(
                            painter = painter,
                            contentDescription = "Anteprima immagine profilo",
                            modifier = Modifier
                                .size(190.dp)
                                .padding(8.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (isValidUrl(photoUrl)) {
                                isUrlValid = true
                                isConfirmed = true
                            } else {
                                isUrlValid = false
                            }
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
