package com.example.vociapp.ui.components.updates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.util.DateTimeFormatter
import com.example.vociapp.data.util.DateTimeFormatterImpl
import com.example.vociapp.data.util.Resource
import com.example.vociapp.ui.viewmodels.HomelessViewModel

@Composable
fun UpdateLastItem(
    updates: Resource<List<Update>>, // Now takes a list of updates
    navController: NavHostController,
    homelessViewModel: HomelessViewModel
) {

    val Updates = remember(updates) { updates.data.orEmpty() }
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterImpl()

    // Get the most recent update (if any)
    val lastUpdate = Updates.maxByOrNull { it.timestamp } ?: return

    //val lastUpdate = updates.maxByOrNull { it.timestamp }

    if (lastUpdate != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = lastUpdate.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = dateTimeFormatter.formatDate(lastUpdate.timestamp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Row {
                        Text(
                            text = lastUpdate.description,
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}