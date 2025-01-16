package com.voci.app.ui.components.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.voci.app.data.local.database.Homeless
import com.voci.app.data.local.database.Request
import com.voci.app.data.util.IconCategory
import com.voci.app.di.LocalServiceLocator
import com.voci.app.ui.components.core.ConfirmButton
import com.voci.app.ui.components.core.DismissButton
import com.voci.app.ui.components.core.hapticFeedback
import com.voci.app.ui.components.homeless.HomelessDialogList
import com.voci.app.ui.components.homeless.HomelessItemUiState
import com.voci.app.ui.components.homeless.HomelessListItem

@Composable
fun AddRequestDialog(
    onDismiss: () -> Unit,                  // Callback to dismiss the dialog
    navController: NavHostController,       // Navigation controller for navigation
) {
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    // Viewmodels
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val requestViewModel = serviceLocator.obtainRequestViewModel()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()
    // Variable used to control the current display of data to modify
    // 1 -> Homeless selection
    // 2 -> Add title, description and icon
    var step by remember { mutableIntStateOf(1) }
    // Homeless Selection Data
    var selectedHomeless by remember { mutableStateOf<Homeless?>(null) }
    LaunchedEffect(Unit) {
        homelessViewModel.updateSearchQuery("")
    }
    // Request Data
    var requestTitle by remember { mutableStateOf("") }
    var requestDescription by remember { mutableStateOf("") }
    var homelessID by remember { mutableStateOf("") }
    var selectedIconCategory by remember { mutableStateOf(IconCategory.OTHER) }
    // Variable used to disable the confirm button during add to database
    var isAddingRequest by remember { mutableStateOf(false) }

    //----- Region: View Composition -----
    AlertDialog(
        // Called when the user tries to dismiss the Dialog by pressing the back button.
        // This is not called when the dismiss button is clicked.
        onDismissRequest = {
            // Action based on step
            when(step) {
                1 -> onDismiss()        // Dismiss dialog if on first step
                2 -> step--             // Step back if on second step
                else -> onDismiss()     // Default
            }
        },
        // Style
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxSize()
        ,
        shape = RoundedCornerShape(0.dp),
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground,
        // Title
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Aggiungi Richiesta",
                    style = MaterialTheme.typography.labelMedium,
                    )
            }
        },
        // Main Content
        text = {
            // Display based on step state
            when (step) {
                // Homeless Selection
                1 -> {
                    selectedHomeless = null

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Seleziona il ricevente",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        DialogSearchBar(
                            onSearch = { homelessViewModel.updateSearchQuery(it) },
                            placeholderText = "Cerca..."
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        HomelessDialogList(
                            onListItemClick = { homeless ->
                                homelessID = homeless.id
                                selectedHomeless = homeless
                                step++
                            },
                            navController = navController
                        )
                    }
                }
                // Add title, description and icon
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Display selected homeless
                        HomelessListItem(
                            homelessState = HomelessItemUiState(homeless = selectedHomeless!!),
                            showPreferredIcon = false,
                            onClick = { step-- },
                            onChipClick = {  },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = requestTitle,
                            onValueChange = { requestTitle = it },
                            label = { Text("Titolo") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                            ),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = requestDescription,
                            onValueChange = { requestDescription = it },
                            label = { Text("Descrizione") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                            ),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        IconSelector(
                            onIconSelected = { iconCategory ->
                                selectedIconCategory = iconCategory
                            },
                            selectedIconCategory = selectedIconCategory
                        )
                    }
                }
            }
        },
        confirmButton = {
            // Confirm button based on step state
            when(step) {
                1 -> {
                    // No need for a confirm button in this step since it moves on the next step
                    // by clicking on a homeless
                }
                // Add button
                2 -> {
                    ConfirmButton(
                        onClick = {
                            // Add request to the database

                            // Disable the button while the request is being added
                            isAddingRequest = true
                            // Create a new request
                            val newRequest = Request(
                                title = requestTitle,
                                description = requestDescription,
                                homelessID = homelessID,
                                creatorId = volunteerViewModel.currentUser.value?.id,
                                iconCategory = selectedIconCategory
                            )
                            // Add the request to the database
                            requestViewModel.addRequest(newRequest)
                            // Dismiss the dialog
                            onDismiss()
                        },
                        modifier = Modifier
                            .hapticFeedback(),
                        enabled =
                        // Enabled only if the request is not being added
                        // and the title and description are not empty
                        !isAddingRequest and
                                requestTitle.isNotEmpty() and
                                requestDescription.isNotEmpty(),
                        text = "Aggiungi"
                    )
                }
            }
        },
        dismissButton = {
            // Dismiss button based on step state
            when(step) {
                // On first step the dismiss button closes the dialog
                1 -> {
                    DismissButton(
                        onClick = { onDismiss() }
                    )
                }
                // On second step the button sends back to first step
                2 -> {
                    DismissButton(
                        onClick = { step-- },
                        text = "Indietro"
                    )
                }
            }
        }
    )
}