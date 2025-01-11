package com.example.vociapp.ui.components.maps

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun StaticMap(latitude: Double, longitude: Double) {
    val context = LocalContext.current
    val token =
        "pk.eyJ1IjoibXNib3JyYSIsImEiOiJjbTUxZzVkaDgxcHAzMmpzZXIycWgyM2hhIn0.kQRnLhjtCyT8l6LRI-B32g"
    val zoom = 14
    val size = "600x300"
    val pin = "pin-s+ff0000($longitude,$latitude)"

    val imageUrl = "https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/" +
            "$pin/" +
            "$longitude,$latitude,$zoom/" +
            size +
            "?access_token=$token"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable {
                val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                try {
                    context.startActivity(mapIntent)
                } catch (e: ActivityNotFoundException) {
                    // Fallback per altri navigatori se Google Maps non è installato
                    val genericUri = Uri.parse("geo:$latitude,$longitude")
                    val genericIntent = Intent(Intent.ACTION_VIEW, genericUri)
                    context.startActivity(genericIntent)
                }
            }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Map location",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
