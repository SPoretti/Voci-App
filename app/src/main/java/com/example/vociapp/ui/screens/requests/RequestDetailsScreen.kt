package com.example.vociapp.ui.screens.requests


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.RequestStatus
import com.example.vociapp.data.util.DateTimeFormatter
import com.example.vociapp.data.util.DateTimeFormatterImpl
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.iconCategoryMap
import com.example.vociapp.ui.components.requests.ModifyRequestDialog
import com.example.vociapp.ui.components.requests.RequestChip
import com.example.vociapp.ui.components.updates.StatusLED

@Composable
fun RequestDetailsScreen(
    requestId: String,
    navController: NavHostController
) {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterImpl()
    val serviceLocator = LocalServiceLocator.current
    val requestViewModel = serviceLocator.getRequestViewModel()

    LaunchedEffect(key1 = requestId) {
        Log.d("RequestDetailsScreen", "Request ID: $requestId")
        requestViewModel.getRequestById(requestId)
    }

    val requestResource by requestViewModel.requestById.collectAsState()

    val homelessViewModel = serviceLocator.getHomelessViewModel()
    val names = homelessViewModel.homelessNames.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }
    var requestForDialog: Request? by remember { mutableStateOf(null) }



    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        FloatingActionButton(
            onClick = {
                if (requestResource is Resource.Success) {
                    requestForDialog = (requestResource as Resource.Success).data
                    showDialog = true
                }
            },
            elevation = FloatingActionButtonDefaults.elevation(50.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary

        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Modify"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (requestResource) {
                is Resource.Loading -> {
                    // Show loading indicator
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    val request = requestResource.data
                    // Access request fields (e.g., request.title, request.description)
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(64.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = iconCategoryMap[request?.iconCategory ?: ""]!!
                                ),
                                contentDescription = "Request icon",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.Center)
                            )
                            when(requestResource.data?.status){
                                RequestStatus.TODO -> {
                                    StatusLED(
                                        color = Color.Green,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .align(Alignment.TopEnd)
                                    )
                                }
                                RequestStatus.DONE -> {
                                    StatusLED(
                                        color = Color.Gray,
                                        isPulsating = false,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .align(Alignment.TopEnd)
                                    )
                                }
                                null -> {

                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = request?.title ?: "Title not available",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = dateTimeFormatter.formatDate(
                                    request?.timestamp ?: 0
                                ) + '\n' +
                                        dateTimeFormatter.formatTime(request?.timestamp ?: 0),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    textAlign = TextAlign.End
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .align(Alignment.CenterVertically)
                            )

                        }
                        Text(
                            text = request?.description ?: "Description not available",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textAlign = TextAlign.Justify
                            ),
                            lineHeight = 1.5.em,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Creata da:", fontSize = 14.sp)
                            RequestChip(
                                text = request?.creatorId.toString(),
                                onClick = { navController.navigate("profileVolontario/${request?.creatorId}") },
                                imageVector = Icons.Filled.Person
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            val homelessName by remember(request?.homelessID) {
                                derivedStateOf {
                                    names[request?.homelessID] ?: "Unknown"
                                }
                            }

                            Text("Ricevente:", fontSize = 14.sp)

                            RequestChip(
                                text = homelessName,
                                onClick = { navController.navigate("profileHomeless/${request?.homelessID}") },
                                imageVector = Icons.Filled.AssignmentInd,
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    // Display error message
                    Text("Error: ${requestResource.message}")
                }
            }
        }
    }
    if (showDialog && requestForDialog != null) {
        ModifyRequestDialog(
            request = requestForDialog!!,
            onDismiss = { showDialog = false },
            navController = navController
        )
    }
}