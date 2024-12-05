package com.example.vociapp.ui.components

import android.util.Log
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.RequestStatus
import com.example.vociapp.data.util.DateTimeFormatter
import com.example.vociapp.data.util.DateTimeFormatterImpl
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.di.ServiceLocator
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.example.vociapp.ui.viewmodels.RequestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RequestListItem(
    request: Request,
    navController: NavHostController,
    requestViewModel: RequestViewModel,
    onClick: () -> Unit
){

    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.getHomelessViewModel()

    var homelessName by remember { mutableStateOf<String?>(null) }

    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterImpl()

    var shouldFetchData by remember { mutableStateOf(true) } // New state variable

    LaunchedEffect(key1 = shouldFetchData) { // Use shouldFetchData as the key
        if (shouldFetchData) {
            try {
                homelessName = withContext(Dispatchers.IO) {
                    getHomelessName(request.homelessID, homelessViewModel)
                }
            } catch (e: Exception) {
                // Handle exception
                Log.e("RequestListItem", "Error getting homeless name", e)
            } finally {
                shouldFetchData = (homelessName == null) // Prevent further fetches
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,

    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
        ) {

            if (request.status == RequestStatus.TODO) {
                IconButton(
                    onClick = { requestDone(request, requestViewModel) },
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
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }else{
                Spacer(modifier = Modifier.padding(8.dp))
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
                    Box(
                        modifier = Modifier.weight(1f)
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

                    Text(
                        text = dateTimeFormatter.formatDate(request.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(6.dp)
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

                    if (homelessName != null) {
                        RequestChip(
                            text = homelessName!!, // Safe to use !! since we checked for null
                            onClick = { navController.navigate("profileHomeless/${request.homelessID}") },
                            imageVector = Icons.Filled.AssignmentInd
                        )
                    }
                }
            }
        }
    }
}

suspend fun getHomelessName(homelessID: String, homelessViewModel: HomelessViewModel): String? {
    return withContext(Dispatchers.IO) {
        homelessViewModel.getHomeless(homelessID)?.name
    }
}

fun requestDone(
    request: Request,
    requestViewModel: RequestViewModel
){
    request.status = RequestStatus.DONE
    requestViewModel.updateRequest(request)
}