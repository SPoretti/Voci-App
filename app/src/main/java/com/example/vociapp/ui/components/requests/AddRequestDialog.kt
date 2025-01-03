package com.example.vociapp.ui.components.requests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.IconCategory
import com.example.vociapp.ui.components.IconSelector
import com.example.vociapp.ui.components.SearchBar
import com.example.vociapp.ui.components.homeless.HomelessList
import com.example.vociapp.ui.components.homeless.HomelessListItem
import com.example.vociapp.ui.components.utils.hapticFeedback
import com.example.vociapp.ui.state.HomelessItemUiState
import com.example.vociapp.ui.viewmodels.VolunteerViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddRequestDialog(
    onDismiss: () -> Unit,
    onAdd: (Request) -> Unit,
    volunteerViewModel: VolunteerViewModel,
    navController: NavHostController,
) {
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()

    var requestTitle by remember { mutableStateOf("") }
    var requestDescription by remember { mutableStateOf("") }
    var homelessID by remember { mutableStateOf("") }

    val homelesses by homelessViewModel.homelesses.collectAsState()
    val filteredHomelesses by homelessViewModel.filteredHomelesses.collectAsState()
    val searchQuery by homelessViewModel.searchQuery.collectAsState()

    var step by remember { mutableIntStateOf(1) }

    var selectedHomeless by remember { mutableStateOf<Homeless?>(null) }

    var isAddingRequest by remember { mutableStateOf(false) }

    var selectedIconCategory by remember { mutableStateOf(IconCategory.OTHER) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Aggiungi Richiesta",
                    modifier = Modifier.wrapContentSize(Alignment.Center)
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
        ,
        shape = RoundedCornerShape(0.dp),
        text = {
            when (step) {
                1 -> {
                    selectedHomeless = null

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Seleziona il ricevente",
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        SearchBar(
                            modifier = Modifier.fillMaxWidth(),
                            onSearch = { homelessViewModel.updateSearchQuery(it) },
                            placeholderText = "Cerca...",
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                            onClick = { },
                            onDismiss = { },
                            navController = navController,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val listToDisplay =
                            if (searchQuery.isBlank())
                                homelesses
                            else
                                filteredHomelesses

                        HomelessList(
                            homelesses = listToDisplay,
                            showPreferredIcon = false,
                            onListItemClick = { homeless ->
                                homelessID = homeless.id
                                selectedHomeless = homeless
                                step++
                            },
                            selectedHomeless = selectedHomeless,
                            navController = navController,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        HomelessListItem(
                            homelessState = HomelessItemUiState(homeless = selectedHomeless!!),
                            showPreferredIcon = false,
                            onClick = {step--},
                            isSelected = true,
                            modifier = Modifier.clip(MaterialTheme.shapes.small)
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        },
        confirmButton = {
            when(step) {
                1 -> {  }
                2 -> {
                    Button(
                        onClick = {
                            isAddingRequest = true
                            val newRequest = Request(
                                title = requestTitle,
                                description = requestDescription,
                                homelessID = homelessID,
                                creatorId = volunteerViewModel.currentUser.value!!.id,
                                iconCategory = selectedIconCategory
                            )
                            onAdd(newRequest)
                        },
                        modifier = Modifier
                            .hapticFeedback(),
                        enabled =
                        !isAddingRequest and
                                requestTitle.isNotEmpty() and
                                requestDescription.isNotEmpty(),
                    ) {
                        Text("Aggiungi")
                    }
                }
            }

        },
        dismissButton = {
            when(step) {
                1 -> {
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
                }
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
        },
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground,
    )
}