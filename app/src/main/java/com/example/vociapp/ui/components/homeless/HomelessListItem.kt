package com.example.vociapp.ui.components.homeless

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.types.UpdateStatus
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
    val volunteerViewModel = serviceLocator.getVolunteerViewModel()
    val userId = volunteerViewModel.currentUser.value?.id
    val userPreferencesResource by volunteerViewModel.userPreferencesResource.collectAsState()

    var isPreferred by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = userPreferencesResource.data, key2 = homelessState) {
        isPreferred = when (userPreferencesResource) {
            is Resource.Success -> userPreferencesResource.data?.contains(homelessState.homeless.id) == true
            else -> false
        }
    }

    Surface(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = {onClick(homelessState.homeless)},
        color = MaterialTheme.colorScheme.background,
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
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(
                        text = homelessState.homeless.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                        }
                    )
                }

                Text(
                    text = homelessState.homeless.location,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 20.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    color = LocalContentColor.current.copy(alpha = 0.6f)
                )

            }

            if (showPreferredIcon){
                IconButton(
                    onClick = {
                        volunteerViewModel.toggleHomelessPreference(userId!!, homelessState.homeless.id)
                        isPreferred = !isPreferred
                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .hapticFeedback(),
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
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.onSurface
                        ,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

//           IconButton(
//                onClick = { /* Your action here */ },
//                modifier = Modifier
//                    .align(Alignment.CenterVertically)
//                    .size(48.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.primary)
//            ) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.Comment,
//                    contentDescription = "Update Icon",
//                    tint = MaterialTheme.colorScheme.surface,
//                    modifier = Modifier.size(32.dp)
//                )
//            }

        }
    }
}