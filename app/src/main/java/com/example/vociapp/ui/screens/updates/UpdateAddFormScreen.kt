package com.example.vociapp.ui.screens.updates

import androidx.compose.foundation.border
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
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
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
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.types.Update
import com.example.vociapp.data.types.UpdateStatus
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.updates.ButtonOption
import com.example.vociapp.ui.components.updates.FormText
import com.example.vociapp.ui.components.updates.StatusLED
import com.example.vociapp.ui.components.utils.hapticFeedback
import com.example.vociapp.ui.viewmodels.AuthViewModel
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.example.vociapp.ui.viewmodels.UpdatesViewModel

@Composable
fun UpdateAddFormScreen(
    navController: NavController,
    buttonOption: ButtonOption,
    homelessId: String
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val serviceLocator = LocalServiceLocator.current
    val updatesViewModel = serviceLocator.getUpdatesViewModel()
    val authViewModel = serviceLocator.getAuthViewModel()
    val homelessViewModel = serviceLocator.getHomelessViewModel()

    var homeless by remember { mutableStateOf<Homeless?>(null) }

    LaunchedEffect(key1 = homelessId) {
        homeless = homelessViewModel.getHomeless(homelessId)
    }

    LaunchedEffect(key1 = buttonOption){
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
                    color = when(buttonOption){
                        ButtonOption.Green -> {
                            Color.Green
                        }
                        ButtonOption.Yellow -> {
                            Color.Yellow
                        }
                        ButtonOption.Red -> {
                            Color.Red
                        }
                        ButtonOption.Gray -> {
                            Color.Gray
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Aggiornamento stato di ${homeless?.name ?: ""}",
                    style = MaterialTheme.typography.headlineMedium
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

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

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))

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

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ){
            FloatingActionButton(
                onClick = {
                    navController.popBackStack()
                },
                elevation = FloatingActionButtonDefaults.elevation(50.dp),
                modifier = Modifier
                    .hapticFeedback(),
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

            FloatingActionButton(
                onClick = {
                    onSend(
                        updatesViewModel,
                        authViewModel,
                        title,
                        description,
                        homeless,
                        homelessViewModel,
                        buttonOption,
                        navController
                    )
                },
                elevation = FloatingActionButtonDefaults.elevation(50.dp),
                modifier = Modifier
                    .hapticFeedback(),
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
    authViewModel: AuthViewModel,
    title: String,
    description: String,
    homeless: Homeless?,
    homelessViewModel: HomelessViewModel,
    buttonOption: ButtonOption,
    navController: NavController
) {
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
    val update = Update(
        title = title,
        description = description,
        homelessID = homeless?.id ?: "",
        creatorId = authViewModel.getCurrentUserProfile()?.displayName ?: "User",
        status = updateStatus
    )
    updatesViewModel.addUpdate(update)

    homeless?.status = updateStatus
    if(homeless != null){
        homelessViewModel.updateHomeless(homeless)
    }

    navController.navigate("Home")
}
