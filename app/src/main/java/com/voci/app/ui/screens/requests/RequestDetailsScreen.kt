package com.voci.app.ui.screens.requests


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
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
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
import com.voci.app.data.local.database.Request
import com.voci.app.data.local.database.RequestStatus
import com.voci.app.data.util.DateTimeFormatter
import com.voci.app.data.util.DateTimeFormatterImpl
import com.voci.app.data.util.Resource
import com.voci.app.di.LocalServiceLocator
import com.voci.app.ui.components.core.CustomChip
import com.voci.app.ui.components.core.StatusLED
import com.voci.app.ui.components.core.SwipeDirection
import com.voci.app.ui.components.core.SwipeableScreen
import com.voci.app.ui.components.requests.DeleteRequestDialog
import com.voci.app.ui.components.requests.ModifyRequestDialog
import com.voci.app.ui.components.requests.iconCategoryMap

@Composable
fun RequestDetailsScreen(
    requestId: String,                  // ID of the request to display
    navController: NavHostController    // Navigation controller for navigation
) {

    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    // DateTimeFormatter
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterImpl()
    // Dialog Variables
    var showModifyDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    // Fetch request by ID
    val requestViewModel = serviceLocator.obtainRequestViewModel()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()

    LaunchedEffect(key1 = requestId) {
        requestViewModel.getRequestById(requestId)
    }
    val requestResource by requestViewModel.requestById.collectAsState()
    val volunteerResource by volunteerViewModel.specificVolunteer.collectAsState()

    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val names = homelessViewModel.homelessNames.collectAsState().value
    var requestForDialog: Request? by remember { mutableStateOf(null) }

    SwipeableScreen(
        navController = navController,
        direction = SwipeDirection.RIGHT,
        targetRoute =
            if(requestResource.data?.status == RequestStatus.TODO){
                "requests"
            } else {
                "requestsHistory"
            }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Details
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Show details based on the Resource Status
                when (requestResource) {
                    // Loading state
                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }
                    // Success state
                    is Resource.Success -> {
                        val request = requestResource.data

                        LaunchedEffect(key1 = request?.creatorId) {
                            request?.creatorId?.let { volunteerViewModel.getVolunteerById(it) }
                        }

                        val volunteer = volunteerResource.data

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            // Icon with Status LED at top right
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
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .align(Alignment.Center)
                                )
                                // Status LED
                                when (requestResource.data?.status) {
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
                            // Title and timestamp
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Title on left
                                Text(
                                    text = request?.title ?: "Title not available",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )
                                // Timestamp on right
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
                            // Description
                            Text(
                                text = request?.description ?: "Description not available",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    textAlign = TextAlign.Justify
                                ),
                                lineHeight = 1.5.em,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            // ---
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(16.dp))

                            // Creator and Recipient
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Creata da:", fontSize = 14.sp)
                                if (volunteer != null) {
                                    CustomChip(
                                        text = volunteer.nickname,
                                        onClick = { navController.navigate("profileVolontario/${request?.creatorId}") },
                                        imageVector = Icons.Filled.Person
                                    )
                                }
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

                                Text("Ricevente:", style = MaterialTheme.typography.labelMedium)

                                CustomChip(
                                    text = homelessName,
                                    onClick = { navController.navigate("profileHomeless/${request?.homelessID}") },
                                    imageVector = Icons.Filled.AssignmentInd,
                                )
                            }
                        }
                    }
                    // Error state: Show a message and a button to leave the screen
                    // Only way this should be triggered is if the request id is invalid somehow
                    is Resource.Error -> {
                        Log.e("RequestDetailsScreen", "Error: ${requestResource.message}")
                        Column {
                            Text("Something went wrong. Please try again later.")
                            Button(onClick = {
                                navController.popBackStack()
                            }) {
                                Text("Go back")
                            }
                        }
                    }
                }
            }
            // Buttons at the bottom based on the status of the request
            when (requestResource.data?.status) {
                RequestStatus.TODO -> {
                    // If the request is not done yet, show the floating buttons to modify or complete it
                    // Bottom Right Floating Button to Modify Request
                    FloatingActionButton(
                        onClick = {
                            if (requestResource is Resource.Success) {
                                requestForDialog = (requestResource as Resource.Success).data
                                showModifyDialog = true
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

                    // Bottom Right Floating Button to Complete Request
                    FloatingActionButton(
                        onClick = {
                            requestViewModel.requestDone(
                                requestResource.data ?: return@FloatingActionButton
                            )
                            navController.navigate("requests")
                        },
                        elevation = FloatingActionButtonDefaults.elevation(50.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Done"
                        )
                    }
                }

                RequestStatus.DONE -> {
                    // If the request is already done, show the floating buttons to delete it or change it to uncompleted
                    // Bottom Right Floating Button to Delete Request
                    FloatingActionButton(
                        onClick = {
                            if (requestResource is Resource.Success) {
                                requestForDialog = (requestResource as Resource.Success).data
                                showDeleteDialog = true
                            }
                        },
                        elevation = FloatingActionButtonDefaults.elevation(50.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.error
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Delete"
                        )
                    }

                    // Bottom Right Floating Button to not Complete Request
                    FloatingActionButton(
                        onClick = {
                            requestViewModel.requestTodo(
                                requestResource.data ?: return@FloatingActionButton
                            )
                            navController.navigate("requests")
                        },
                        elevation = FloatingActionButtonDefaults.elevation(50.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Undo"
                        )
                    }
                }

                null -> {

                }
            }
        }
    }

    // Logic to show dialog when the floating button to modify is pressed
    if (showModifyDialog && requestForDialog != null) {
        ModifyRequestDialog(
            request = requestForDialog!!,
            onDismiss = { showModifyDialog = false },
            navController = navController
        )
    }

    // Logic to show dialog when the floating button to modify is pressed
    if (showDeleteDialog && requestForDialog != null) {
        DeleteRequestDialog(
            request = requestForDialog!!,
            onDismiss = {
                showDeleteDialog = false
            },
            navController = navController
        )
    }
}