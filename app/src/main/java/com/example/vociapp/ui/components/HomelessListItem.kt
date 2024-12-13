package com.example.vociapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.state.HomelessItemUiState

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomelessListItem(
    homelessState: MutableState<HomelessItemUiState>,
    showPreferredIcon: Boolean,
    onClick:(Homeless) -> Unit,
    isSelected: Boolean = false,
){

    val serviceLocator = LocalServiceLocator.current
    val volunteerViewModel = serviceLocator.getVolunteerViewModel()
    val userId = volunteerViewModel.currentUser.value?.id
    val userPreferencesResource by volunteerViewModel.userPreferencesResource.collectAsState()
    var isPreferred by remember { mutableStateOf(false) }
    val backgroundColor =
        if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        }

    LaunchedEffect(userPreferencesResource.data) { // Trigger recomposition on state change
        isPreferred = when (userPreferencesResource) {
            is Resource.Success -> userPreferencesResource.data?.contains(homelessState.value.homeless.id) == true
            else -> false
        }
    }

    Surface(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .height(80.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = {onClick(homelessState.value.homeless)},
        shadowElevation = 4.dp,
        color = backgroundColor,
    ){

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){

            Column(
                modifier = Modifier.weight(1f),
            ) {

                Text(
                    text = homelessState.value.homeless.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp),
                )

                Text(
                    text = homelessState.value.homeless.location,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 20.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp),

                )

            }

            if (showPreferredIcon){
                IconButton(
                    onClick = {
                        volunteerViewModel.toggleHomelessPreference(userId!!, homelessState.value.homeless.id)
                        isPreferred = !isPreferred
                    },
                    modifier = Modifier.align(Alignment.CenterVertically),
                ) {
                    Icon(
                        imageVector =
                        if (isPreferred){
                            Icons.Filled.Star
                        } else {
                            Icons.Filled.StarOutline
                        },
                        contentDescription = "Preferred icon",
                        tint =
                        if (isPreferred)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                        ,
                        modifier = Modifier.size(32.dp)
                    )
//                    Icon(
//                        imageVector =
//                        if (homelessState.value.isPreferred)
//                            Icons.Filled.Star
//                        else
//                            Icons.Filled.StarOutline,
//                        contentDescription = "Preferred icon",
//                        tint = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.size(32.dp)
//                    )
                }
            }
        }

    }

}