package com.example.vociapp.ui.components

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    initialPhotoUrl: String
) {
    var photoUrl by remember { mutableStateOf(initialPhotoUrl) }
    var isConfirmed by remember { mutableStateOf(false) }
    var isUrlValid by remember { mutableStateOf(true) }
    var step by remember { mutableIntStateOf(1) }
    var photoNotAvailable by remember { mutableStateOf(false) }

    fun isValidImageUrl(url: String): Boolean {

        if (url.isEmpty()) {
            return true
        }

        val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }

        val imageExtensions = listOf(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp")
        return Patterns.WEB_URL.matcher(formattedUrl).matches() &&
                imageExtensions.any { formattedUrl.lowercase().endsWith(it) }
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
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (step) {
                        1 -> {
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

                            Spacer(modifier = Modifier.height(16.dp))

                            if (isUrlValid && photoUrl.isNotEmpty()) {
                                val painter = rememberAsyncImagePainter(
                                    model = photoUrl,
                                    onError = {photoNotAvailable = true}
                                )
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

                            Button(
                                onClick = {
                                    if (isValidImageUrl(photoUrl)) {
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

                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onBackground,
                                ),
                            ) {
                                Text("Chiudi")
                            }

                            TextButton(
                                onClick = { step++ },
                                modifier = Modifier
                                    .wrapContentSize()
                            ) {
                                Text(
                                    "Note sull'immagine profilo",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                        }

                        2 -> {
                            Text(
                                "Note sull'immagine profilo",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                "Non è possibile caricare immagini da alcuni URL a causa di limitazioni tecniche. " +
                                        "Ti invitiamo a utilizzare un URL alternativo oppure lasciare il campo vuoto.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedButton(
                                onClick = { step-- },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onBackground,
                                ),
                                )
                            {
                                Text("Indietro")
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}
