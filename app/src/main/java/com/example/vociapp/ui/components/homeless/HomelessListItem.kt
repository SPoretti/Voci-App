package com.example.vociapp.ui.components.homeless

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.UpdateStatus
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.core.CustomChip
import com.example.vociapp.ui.components.core.StatusLED
import com.example.vociapp.ui.components.core.hapticFeedback

@Composable
fun HomelessListItem(
    homelessState: HomelessItemUiState,     // Data to display
    showPreferredIcon: Boolean,             // Whether to show the preferred icon
    onClick:(Homeless) -> Unit,             // Callback to handle item click
    onChipClick:() -> Unit                  // Callback to handle chip click
){
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    // Favorites Data
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()
    val isPreferredFlow = volunteerViewModel.isHomelessPreferred(homelessState.homeless.id)
    val isPreferred by isPreferredFlow.collectAsState(initial = false)

    //----- Region: View Composition -----
    Surface(
        modifier = Modifier
            .height(75.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick(homelessState.homeless) },
        color = MaterialTheme.colorScheme.background,
    ){
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(0.7f),
            ) {
                // Name, StatusLED
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(
                        text = homelessState.homeless.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, false)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // StatusLED with style based on homeless status
                    StatusLED(
                        // Map the status to the color
                        color = when (homelessState.homeless.status){
                            UpdateStatus.GREEN -> Color.Green
                            UpdateStatus.YELLOW -> Color.Yellow
                            UpdateStatus.RED -> Color.Red
                            UpdateStatus.GRAY -> Color.Gray
                        },
                        // If color is gray don't pulsate
                        isPulsating = when(homelessState.homeless.status){
                            UpdateStatus.GREEN -> true
                            UpdateStatus.YELLOW -> true
                            UpdateStatus.RED -> true
                            UpdateStatus.GRAY -> false
                        },
                    )
                }
                // Age, Gender, Nationality dim color
                Text(
                    text =
                    "${homelessState.homeless.gender.displayName}, " +
                    "${homelessState.homeless.age},  " +
                    "${homelessState.homeless.nationality} "
                    ,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f, false)
                )
            }
            // Location, Preferred Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(1f)
            ){
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {// Chip for the location, onClick: sends to the map
                    CustomChip(
                        text = homelessState.homeless.location,
                        imageVector = Icons.Filled.LocationOn,
                        onClick = onChipClick
                    )
                }
                // Preferred Icon, display based on the prop value since it is used both in the
                // home screen and in the request add/modify dialogs
                if (showPreferredIcon) {
                    IconButton(
                        onClick = {
                            volunteerViewModel.toggleHomelessPreference(
                                homelessState.homeless.id
                            )
                            //isPreferred = !isPreferred
                        },
                        modifier = Modifier.hapticFeedback(),
                    ) {
                        Icon(
                            imageVector =
                            if (isPreferred)
                                Icons.Filled.Star
                            else
                                Icons.Filled.StarOutline,
                            contentDescription = "Preferred icon",
                            tint =
                            if (isPreferred)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}