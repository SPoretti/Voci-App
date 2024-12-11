package com.example.vociapp.ui.components.requests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vociapp.data.types.Request
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RequestCard(request: Request) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.padding(8.dp).clickable {  },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = request.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = request.description)
            //Text(text = "Status: ${request.status.name}")
            Text(text = "Creata da: ${request.creatorId}")
            Text(text = "Senzatetto: ${request.homelessID}")
            Text(
                text = "Data e ora: ${
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                        Date(request.timestamp)
                    )
                }"
            )
        }
    }
}