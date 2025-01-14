package com.example.vociapp.ui.screens.updates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.local.database.UpdateStatus
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.core.StatusLED
import com.example.vociapp.ui.components.core.hapticFeedback
import com.example.vociapp.ui.components.updates.ButtonOption
import com.example.vociapp.ui.components.updates.FormText
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.example.vociapp.ui.viewmodels.UpdatesViewModel
import com.example.vociapp.ui.viewmodels.VolunteerViewModel

@Composable
fun UpdateAddFormScreen(
    navController: NavController,   // Navigation controller for navigating between screens
    buttonOption: ButtonOption,     // Button option for the update
    homelessId: String              // ID of the homeless being updated
) {
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    val updatesViewModel = serviceLocator.obtainUpdatesViewModel()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    // Form data
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var homeless by remember { mutableStateOf<Homeless?>(null) }
    // Get homeless data
    LaunchedEffect(homelessId) {
        homeless = homelessViewModel.getHomelessById(homelessId)
    }
    // Set form data based on button option
    LaunchedEffect(buttonOption){
        when (buttonOption) {
            ButtonOption.Green -> {
                title = FormText.GREEN_TITLE
                description = FormText.GREEN_DESCRIPTION
            }

            ButtonOption.Yellow -> {
                title = FormText.YELLOW_TITLE
                description = FormText.YELLOW_DESCRIPTION
            }

            ButtonOption.Red -> {
                title = FormText.RED_TITLE
                description = FormText.RED_DESCRIPTION
            }

            ButtonOption.Gray -> {
                title = FormText.GRAY_TITLE
                description = FormText.GRAY_DESCRIPTION
            }
        }
    }

    //----- Region: View Composition -----
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(modifier = Modifier.padding(48.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                StatusLED(
                    // Set color based on button option
                    color = when(buttonOption){
                        ButtonOption.Green ->   { Color.Green }
                        ButtonOption.Yellow ->  { Color.Yellow }
                        ButtonOption.Red ->     { Color.Red }
                        ButtonOption.Gray ->    { Color.Gray }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(16.dp))
                // Set title based on button option
                Text(
                    text = "Aggiornamento stato di ${homeless?.name ?: ""}",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Form fields
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titolo") },
                maxLines = 2,
                minLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            //---
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            //---
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrizione") },
                maxLines = 10,
                minLines = 5,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        // Send and Cancel buttons
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ){
            // Cancel button
            FloatingActionButton(
                onClick = {
                    navController.popBackStack()
                },
                elevation = FloatingActionButtonDefaults.elevation(50.dp),
                modifier = Modifier.hapticFeedback(),
                containerColor = MaterialTheme.colorScheme.surface

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Annulla",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Annulla",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Send button
            FloatingActionButton(
                onClick = {
                    onSend(
                        updatesViewModel,
                        volunteerViewModel,
                        title,
                        description,
                        homeless,
                        homelessViewModel,
                        buttonOption,
                        navController
                    )
                },
                elevation = FloatingActionButtonDefaults.elevation(50.dp),
                modifier = Modifier.hapticFeedback(),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Aggiungi",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Aggiungi Commento",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

fun onSend(
    updatesViewModel: UpdatesViewModel,
    volunteerViewModel: VolunteerViewModel,
    title: String,
    description: String,
    homeless: Homeless?,
    homelessViewModel: HomelessViewModel,
    buttonOption: ButtonOption,
    navController: NavController
) {
    // Set update status based on button option
    var updateStatus = UpdateStatus.GREEN
    updateStatus = when (buttonOption) {
        ButtonOption.Green -> {
            UpdateStatus.GREEN
        }
        ButtonOption.Yellow -> {
            UpdateStatus.YELLOW
        }
        ButtonOption.Red -> {
            UpdateStatus.RED
        }
        ButtonOption.Gray -> {
            UpdateStatus.GRAY
        }
    }
    // Create update
    val update = Update(
        title = title,
        description = description,
        homelessID = homeless!!.id,
        creatorId = volunteerViewModel.currentUser.value.data?.id.toString(),
        status = updateStatus
    )
    // Add update to database
    updatesViewModel.addUpdate(update)
    // Update homeless status
    homeless.status = updateStatus
    homelessViewModel.updateHomeless(homeless)
    // Navigate back to home
    navController.navigate("Home")
}
