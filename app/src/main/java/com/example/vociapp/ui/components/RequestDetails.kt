package com.example.vociapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.util.DateTimeFormatter
import com.example.vociapp.data.util.DateTimeFormatterImpl

@Composable
fun RequestDetails(request: Request, onDismiss: () -> Unit, navController: NavHostController) {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterImpl()
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = request.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp).widthIn(max = 160.dp),
                )
                Text(
                    text = request.description,
                    fontSize = 16.sp,
                    lineHeight = 1.5.em
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Created by:", fontSize = 14.sp)
                    RequestChip(
                        text = request.creatorId.toString(),
                        onClick = { navController.navigate("profileVolontario/${request.creatorId}") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Homeless:", fontSize = 14.sp)
                    RequestChip(
                        text = request.homelessID.toString(),
                        onClick = { navController.navigate("profileHomeless/${request.homelessID}") },
                    )
                }
            }
            Text(
                text = dateTimeFormatter.formatTimestamp(request.timestamp),
                modifier = Modifier
                    .align(Alignment.TopEnd),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}