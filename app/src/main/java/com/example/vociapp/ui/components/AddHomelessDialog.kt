package com.example.vociapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.vociapp.data.types.Gender
import com.example.vociapp.data.types.Homeless

@Composable
fun AddHomelessDialog(
    onDismiss: () -> Unit,
    onAdd: (Homeless) -> Unit
) {
    var homelessName by remember { mutableStateOf("") }
    var homelessLocation by remember { mutableStateOf("") }
    var homelessPets by remember { mutableStateOf("No") }
    var homelessAge by remember { mutableStateOf("") }
    var homelessNationality by remember { mutableStateOf("") }
    var homelessDescription by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(Gender.Unspecified) }

    var isAddingHomeless by remember { mutableStateOf(false) }

    AlertDialog(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = { Text("Aggiungi Senzatetto") },
        text = {

            Column(
                modifier = Modifier.padding(16.dp),
            ) {

                OutlinedTextField(
                    value = homelessName,
                    onValueChange = { homelessName = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = homelessLocation,
                    onValueChange = { homelessLocation = it },
                    label = { Text("Luogo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = homelessPets,
                    onValueChange = { homelessPets = it },
                    label = { Text("Animali") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = homelessAge,
                    onValueChange = { homelessAge = it },
                    label = { Text("Età") },
                    placeholder = { Text("40-45,  53,  60-70, ...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = homelessNationality,
                    onValueChange = { homelessNationality = it },
                    label = { Text("Nazionalità") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                GenderSelector(
                    selectedGender = selectedGender,
                    onGenderSelected = { selectedGender = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = homelessDescription,
                        onValueChange = { homelessDescription = it },
                        label = { Text("Descrizione") },
                        maxLines = 10,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        ),
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isAddingHomeless = true
                    val newHomeless = Homeless(
                        name = homelessName,
                        gender = selectedGender,
                        location = homelessLocation,
                        age = homelessAge,
                        pets = homelessPets,
                        description = homelessDescription
                    )
                    onAdd(newHomeless)
                },
                enabled =
                    !isAddingHomeless and
                    homelessName.isNotEmpty() and
                    homelessLocation.isNotEmpty(),
            ) {
                Text("Aggiungi")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {onDismiss()},
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
           ) {
                Text("Annulla")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        textContentColor = MaterialTheme.colorScheme.onBackground,
    )
}