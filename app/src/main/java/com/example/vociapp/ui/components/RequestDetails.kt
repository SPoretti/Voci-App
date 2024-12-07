package com.example.vociapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Person
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ){
                        Text(
                            text = request.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Box(
                        contentAlignment = Alignment.TopEnd // Allinea il contenuto a destra
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End, // Allinea i testi a destra
                            verticalArrangement = Arrangement.spacedBy(6.dp) // Spazio tra le righe
                        ) {
                            Text(
                                text = dateTimeFormatter.formatDate(request.timestamp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                            )

                            Text(
                                text = dateTimeFormatter.formatTime(request.timestamp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }

                }
                Text(
                    text = request.description,
                    fontSize = 16.sp,
                    lineHeight = 1.5.em
                )
                Text(
                    text = request.status.toString(),
                    fontSize = 16.sp,
                    lineHeight = 1.5.em
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Creato da:", fontSize = 14.sp)
                    RequestChip(
                        text = request.creatorId.toString(),
                        onClick = { navController.navigate("profileVolontario/${request.creatorId}") },
                        imageVector = Icons.Filled.Person
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Senzatetto:", fontSize = 14.sp)
                    RequestChip(
                        text = request.homelessID.toString(),
                        onClick = { navController.navigate("profileHomeless/${request.homelessID}") },
                        imageVector = Icons.Filled.AssignmentInd
                    )
                }
            }
        }
    }
}