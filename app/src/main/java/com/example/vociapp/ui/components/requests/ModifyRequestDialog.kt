package com.example.vociapp.ui.components.requests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.types.Request
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.SearchBar
import com.example.vociapp.ui.components.homeless.HomelessList
import com.example.vociapp.ui.viewmodels.AuthViewModel

@Composable
fun ModifyRequestDialog(
    onDismiss: () -> Unit,
    navController: NavHostController,
    request: Request
) {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.getHomelessViewModel()
    val requestViewModel = serviceLocator.getRequestViewModel()

    var requestTitle by remember { mutableStateOf(request.title) }
    var requestDescription by remember { mutableStateOf(request.description) }
    var homelessID by remember { mutableStateOf(request.homelessID) }

    val homelesses by homelessViewModel.homelesses.collectAsState()
    val filteredHomelesses by homelessViewModel.filteredHomelesses.collectAsState()
    val searchQuery by homelessViewModel.searchQuery.collectAsState()
    var selectedHomeless by remember { mutableStateOf<Homeless?>(null) }

    var isAddingRequest by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(true) }


    if(showDialog) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Modifica Richiesta",
                    modifier = Modifier.wrapContentSize(Alignment.Center)
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

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

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                SearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    onSearch = { homelessViewModel.updateSearchQuery(it) },
                    placeholderText = "Cerca un senzatetto...",
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    onClick = { }
                )

                Spacer(modifier = Modifier.height(16.dp))

                val listToDisplay =
                    if (searchQuery.isBlank())
                        homelesses
                    else
                        filteredHomelesses

                HomelessList(
                    homelesses = listToDisplay,
                    homelessViewModel = homelessViewModel,
                    navController = navController,
                    showPreferredIcon = false,
                    onListItemClick = { homeless ->
                        homelessID = homeless.id
                        selectedHomeless = homeless
                    },
                    selectedHomeless = selectedHomeless,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isAddingRequest = true
                    request.title = requestTitle
                    request.description = requestDescription
                    request.homelessID = homelessID

                    requestViewModel.updateRequest(request)
                },
                enabled =
                !isAddingRequest and
                        requestTitle.isNotEmpty() and
                        requestDescription.isNotEmpty(),
            ) {
                Text("Modifica")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {onDismiss()},
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            ) {
                Text("Annulla")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        textContentColor = MaterialTheme.colorScheme.onBackground,
    )
    }
}