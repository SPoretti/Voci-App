package com.example.vociapp.ui.components.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.util.DateTimeFormatter
import com.example.vociapp.data.util.DateTimeFormatterImpl
import com.example.vociapp.ui.viewmodels.HomelessViewModel

@Composable
fun RequestListItem(
    request: Request,
    navController: NavHostController,
    homelessViewModel: HomelessViewModel
){

    val names = homelessViewModel.homelessNames.collectAsState().value

    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterImpl()


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(navController, request) },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
    ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize(),
        ) {

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    painter = painterResource(id = iconCategoryMap[request.iconCategory]!!),
                    contentDescription = "Request icon",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = dateTimeFormatter.formatDate(request.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Row{
                    Text(
                        text = request.description,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row (verticalAlignment = Alignment.CenterVertically) {

                    val homelessName by remember(request.homelessID) {
                        derivedStateOf {
                            names[request.homelessID] ?: "Unknown"
                        }
                    }

                    RequestChip(
                        text = homelessName,
                        onClick = { navController.navigate("profileHomeless/${request.homelessID}") },
                        imageVector = Icons.Filled.AssignmentInd
                    )

                }
            }
        }
    }
}

fun onClick(navController: NavHostController, request: Request) {
    navController.navigate("requestDetailsScreen/${request.id}")
}

