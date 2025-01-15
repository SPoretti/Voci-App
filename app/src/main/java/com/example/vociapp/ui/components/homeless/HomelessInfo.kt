package com.example.vociapp.ui.components.homeless

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.UpdateStatus
import com.example.vociapp.data.util.Gender
import com.example.vociapp.ui.components.core.CustomChip
import com.example.vociapp.ui.components.core.StatusLED

// INFO Principali del senzatetto
@Composable
fun HomelessInfo(
    homeless: Homeless,
    openDescription: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = homeless.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.weight(1f, fill = false)
            )
            StatusLED(
                // Map the status to the color
                color = when (homeless.status) {
                    UpdateStatus.GREEN -> Color.Green
                    UpdateStatus.YELLOW -> Color.Yellow
                    UpdateStatus.RED -> Color.Red
                    UpdateStatus.GRAY -> Color.Gray
                },
                // If color is gray don't pulsate
                isPulsating = when (homeless.status) {
                    UpdateStatus.GREEN -> true
                    UpdateStatus.YELLOW -> true
                    UpdateStatus.RED -> true
                    UpdateStatus.GRAY -> false
                }
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            CustomChip(
                text = if(homeless.age == "") "?" else ("${homeless.age} anni"),
                onClick = {},
                imageVector = Icons.Default.CalendarMonth
            )
            Spacer(modifier = Modifier.width(4.dp))
            when(homeless.gender){
                Gender.Female -> {
                    CustomChip(
                        text = "Femmina",
                        onClick = {},
                        imageVector = Icons.Default.Female,
                    )
                }
                Gender.Male -> {
                    CustomChip(
                        text = "Maschio",
                        onClick = {},
                        imageVector = Icons.Default.Male
                    )
                }
                Gender.Unspecified -> {
                    CustomChip(
                        text = "?",
                        onClick = {},
                        imageVector = Icons.Default.Transgender
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            CustomChip(
                text = if(homeless.nationality == "") "?" else (homeless.nationality),
                onClick = {},
                imageVector = Icons.Default.Public
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = homeless.description,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    openDescription()
                },
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = homeless.location,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}