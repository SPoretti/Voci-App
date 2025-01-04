package com.example.vociapp.ui.components.requests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.homeless.HomelessList

@Composable
fun ModifyRequestDialog(
    onDismiss: () -> Unit,              // Called when the dialog is dismissed
    navController: NavHostController,   // Navigation controller for navigation
    request: Request                    // Request to be modified
) {
    //----- Region: Data Initialization -----

    val serviceLocator = LocalServiceLocator.current
    // Viewmodels
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val requestViewModel = serviceLocator.obtainRequestViewModel()
    // Request Data
    var requestTitle by remember { mutableStateOf(request.title) }
    var requestDescription by remember { mutableStateOf(request.description) }
    var homelessID by remember { mutableStateOf(request.homelessID) }
    var selectedIconCategory by remember { mutableStateOf(request.iconCategory) }
    // Homeless Selection Data
    val homelesses by homelessViewModel.homelesses.collectAsState()
    val filteredHomelesses by homelessViewModel.filteredHomelesses.collectAsState()
    val searchQuery by homelessViewModel.searchQuery.collectAsState()
    var selectedHomeless by remember { mutableStateOf<Homeless?>(null) }
    // Variable used to disable the confirm button during add to database
    var isAddingRequest by remember { mutableStateOf(false) }
    // Variable used to control the current display of data to modify
    // 1 -> Homeless selection
    // 2 -> Modify title and description
    var step by remember { mutableIntStateOf(1) }

    //----- Region: View Composition -----

    AlertDialog(
        // Called when the user tries to dismiss the Dialog by pressing the back button.
        // This is not called when the dismiss button is clicked.
        onDismissRequest = { onDismiss() },
        // Style
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(0.dp),
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground,
        // Title of the dialog
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Modifica Richiesta",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        },
        // Content
        text = {
            // Display based on step state
            when(step){
                // Selection of the homeless
                1 -> {
                    selectedHomeless = null

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Searchbar for homeless selection
                        DialogSearchBar(
                            onSearch = { homelessViewModel.updateSearchQuery(it) },
                            placeholderText = "Cerca..."
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // List of homelesses to select, updates based on search query
                        // Goes to next step on selection
                        val listToDisplay =
                            if (searchQuery.isBlank())
                                homelesses
                            else
                                filteredHomelesses
                        HomelessList(
                            homelesses = listToDisplay,
                            showPreferredIcon = false,
                            onListItemClick = { homeless ->
                                selectedHomeless = homeless
                                step++
                            },
                            selectedHomeless = selectedHomeless,
                            navController = navController,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                // Modify title and description
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Modify title starts with the current title
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

                        // Modify description starts with the current description
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

                        // Modify icon starts with the current icon
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
            // Confirm button based on step
            when(step){
                1 -> {
                    // No need for a confirm button in this step since it moves on the next step
                    // by clicking on a homeless
                }
                // Modify button
                2 -> {
                    Button(
                        onClick = {
                            // Update request in the database

                            // Disable the button while the request is being added
                            isAddingRequest = true

                            // modify the current request with the updated data
                            request.title = requestTitle
                            request.description = requestDescription
                            request.homelessID = selectedHomeless?.id ?: homelessID

                            // Update the databases
                            requestViewModel.updateRequest(request)

                            onDismiss()
                        },
                        // Enabled only if the request is not being added
                        // and the title and description are not empty
                        enabled =
                        !isAddingRequest and
                                requestTitle.isNotEmpty() and
                                requestDescription.isNotEmpty(),
                    ) {
                        Text("Modifica")
                    }
                }
            }

        },
        dismissButton = {
            // Dismiss button based on step
            when(step) {
                // On first step the dismiss button closes the dialog
                1 -> {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                    ) {
                        Text("Annulla")
                    }
                }
                // On second step the button sends back to first step
                2 -> {
                    OutlinedButton(
                        onClick = { step = 1 },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                    ) {
                        Text("Annulla")
                    }
                }
            }
        }
    )
}
