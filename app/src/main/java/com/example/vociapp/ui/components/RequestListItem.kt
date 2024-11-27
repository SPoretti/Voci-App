package com.example.vociapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.RequestStatus
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RequestListItem(request: Request, navController: NavHostController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            IconButton(
                onClick = { request.status = RequestStatus.DONE },
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Request icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(
                            text = request.title,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = request.description, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Row {
                    RequestChip(
                        text = request.creatorId.toString(),
                        isSelected = request.status == RequestStatus.DONE,
                        onClick = { navController.navigate("profileVolontario/${request.creatorId}") }
                    )
                    RequestChip(
                        text = request.homelessID.toString(),
                        isSelected = request.status == RequestStatus.DONE,
                        onClick = { navController.navigate("profileHomeless/${request.homelessID}") }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = formatTimestamp(request.timestamp),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Bottom)
            )
        }

    }
}

fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(timestamp)
}
