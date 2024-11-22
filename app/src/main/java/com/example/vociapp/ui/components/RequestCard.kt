package com.example.vociapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vociapp.data.model.Request
import com.example.vociapp.data.model.RequestStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RequestCard(request: Request) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = request.title, fontWeight = FontWeight.Bold)
            Text(text = request.description)
            Text(text = "Status: ${request.status.name}", color = when (request.status) {
                RequestStatus.PENDING -> Color.Gray
                RequestStatus.APPROVED -> Color.Green
                RequestStatus.REJECTED -> Color.Red
            })
            Text(
                text = "Timestamp: ${
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                        Date(request.timestamp)
                    )
                }"
            )
        }
    }
}