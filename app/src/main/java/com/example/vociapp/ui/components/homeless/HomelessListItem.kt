package com.example.vociapp.ui.components.homeless

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.UpdateStatus
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.updates.StatusLED
import com.example.vociapp.ui.components.utils.hapticFeedback
import com.example.vociapp.ui.state.HomelessItemUiState

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomelessListItem(
    homelessState: HomelessItemUiState,
    showPreferredIcon: Boolean,
    onClick:(Homeless) -> Unit,
    isSelected: Boolean = false,
){

    val serviceLocator = LocalServiceLocator.current
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()
    val userId = volunteerViewModel.currentUser.value?.id
    val userPreferencesResource by volunteerViewModel.userPreferencesResource.collectAsState()
    val backgroundColor =
        if (isSelected) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.background
        }

    var isPreferred by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = userPreferencesResource.data, key2 = homelessState) {
        isPreferred = when (userPreferencesResource) {
            is Resource.Success -> userPreferencesResource.data?.contains(homelessState.homeless.id) == true
            else -> false
        }
    }

    Surface(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = {onClick(homelessState.homeless)},
        color = backgroundColor,
    ){

        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Column(
                modifier = Modifier.weight(1f),
            ) {
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
                    StatusLED(
                        color = when (homelessState.homeless.status){
                            UpdateStatus.GREEN -> Color.Green
                            UpdateStatus.YELLOW -> Color.Yellow
                            UpdateStatus.RED -> Color.Red
                            UpdateStatus.GRAY -> Color.Gray
                        },
                        isPulsating = when(homelessState.homeless.status){
                            UpdateStatus.GREEN -> true
                            UpdateStatus.YELLOW -> true
                            UpdateStatus.RED -> true
                            UpdateStatus.GRAY -> false
                        },
                    )
                }

                HomelessChip(
                    text = homelessState.homeless.location,
                    imageVector = Icons.Filled.LocationOn,
                    onClick = {},
                    modifier = Modifier
                )
            }

            Box(
                contentAlignment = Alignment.Center
            ){

                if (showPreferredIcon) {
                    IconButton(
                        onClick = {
                            volunteerViewModel.toggleHomelessPreference(
                                userId!!,
                                homelessState.homeless.id
                            )
                            isPreferred = !isPreferred
                        },
                        modifier = Modifier
                            .hapticFeedback(),
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