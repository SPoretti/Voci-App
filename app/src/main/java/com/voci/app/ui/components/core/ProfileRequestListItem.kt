package com.voci.app.ui.components.core

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
import com.voci.app.data.local.database.Request
import com.voci.app.data.util.DateTimeFormatter
import com.voci.app.data.util.DateTimeFormatterImpl
import com.voci.app.di.LocalServiceLocator
import com.voci.app.ui.components.requests.iconCategoryMap

@Composable
fun ProfileRequestListItem(
    request: Request,
    navController: NavHostController
){
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    // Map of names of the homeless to get the name of the homeless
    val names = homelessViewModel.homelessNames.collectAsState().value
    val homelessName by remember(request.homelessID) {
        derivedStateOf {
            names[request.homelessID] ?: "Unknown"
        }
    }
    // Date formatter
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterImpl()

    //----- Region: View Composition -----
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("requestDetailsScreen/${request.id}")
            },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 16.dp,
    ) {
        // Display request
        Row(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            // Icon based on the category at the beginning
            Box(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, end = 0.dp, start = 12.dp)
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
            // Details of the request
            Column(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, end = 12.dp, start = 12.dp),
                horizontalAlignment = Alignment.Start,
            ){
                // Title and date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = request.title,
                        style = MaterialTheme.typography.titleMedium.copy(
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
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Text(
                    text = request.description,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

