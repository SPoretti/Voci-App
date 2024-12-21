package com.example.vociapp.ui.components.homeless

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.vociapp.data.types.Homeless

@Composable
fun AddHomelessDialog(
    onDismiss: () -> Unit,
    onAdd: (Homeless) -> Unit
) {
    var nameError by remember { mutableStateOf<String?>(null) }
    var locationError by remember { mutableStateOf<String?>(null) }

    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 4
    val homeless = remember { Homeless() }

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
                when (currentStep) {
                    1 -> Step1(homeless, nameError, locationError) { name, location ->
                        // Aggiorna errori in tempo reale durante la modifica
                        nameError = if (name.isBlank()) "Inserire il nome" else null
                        locationError = if (location.isBlank()) "Inserire il luogo" else null
                    }
                    2 -> Step2(homeless)
                    3 -> Step3(homeless)
                    4 -> Step4(homeless)
                }
            }
        },
        confirmButton = {
            if (currentStep < totalSteps) {
                Button(
                    onClick = {
                        if (currentStep == 1) {
                            // Validazione dei campi di Step1
                            var hasError = false
                            if (homeless.name.isBlank()) {
                                nameError = "Inserire il nome"
                                hasError = true
                            }
                            if (homeless.location.isBlank()) {
                                locationError = "Inserire il luogo"
                                hasError = true
                            }
                            if (!hasError) {
                                currentStep++
                            }
                        } else {
                            currentStep++
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor =MaterialTheme.colorScheme.onPrimary,
                    )
                ) {
                    Text("Avanti")
                }
            } else {
                Button(
                    onClick = {
                        isAddingHomeless = true
                        onAdd(homeless)
                    },
                ) {
                    Text("Aggiungi")
                }
            }
        },
        dismissButton = {
            if (currentStep > 1) {
                OutlinedButton(
                    onClick = { currentStep-- },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
                ) {
                    Text("Indietro")
                }
            } else {
                OutlinedButton(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
                ) {
                    Text("Annulla")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        textContentColor = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
fun Step1(
    homeless: Homeless,
    nameError: String?,
    locationError: String?,
    onFieldsChanged: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(homeless.name) }
    var location by remember { mutableStateOf(homeless.location) }

    OutlinedTextField(
        value = name,
        onValueChange = {
            name = it
            homeless.name = it
            onFieldsChanged(it, location) // Aggiorna errori in tempo reale
        },
        label = { Text("Nome") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        ),
        isError = nameError != null,
    )

    if (nameError != null) {
        Text(nameError, color = Color.Red)
    }
    else
        Spacer(modifier = Modifier.height(20.dp))

    OutlinedTextField(
        value = location,
        onValueChange = {
            location = it
            homeless.location = it
            onFieldsChanged(name, it) // Aggiorna errori in tempo reale
        },
        label = { Text("Luogo") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        ),
        isError = locationError != null,
    )
    if (locationError != null) {
        Text(locationError, color = Color.Red)
    }
    else
        Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun Step2(homeless: Homeless) {

    var age by remember { mutableStateOf(homeless.age) }
    var nationality by remember { mutableStateOf(homeless.nationality) }

    OutlinedTextField(
        value = age,
        onValueChange = {
            age = it
            homeless.age = it
        },
        label = { Text("Età") },
        placeholder = { Text("40-45,  53,  60-70, ...") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        ),
    )

    Spacer(modifier = Modifier.height(20.dp))

    OutlinedTextField(
        value = nationality,
        onValueChange = {
            nationality = it
            homeless.nationality = it
        },
        label = { Text("Nazionalità") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        ),
    )
}

@Composable
fun Step3(homeless: Homeless) {

    var pets by remember { mutableStateOf(homeless.pets) }
    var gender by remember { mutableStateOf(homeless.gender) }

    OutlinedTextField(
        value = pets,
        onValueChange = {
            pets = it
            homeless.pets = it
        },
        label = { Text("Animali") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        ),
    )

    Spacer(modifier = Modifier.height(20.dp))

    GenderSelector(
        selectedGender = gender,
        onGenderSelected = {
            gender = it
            homeless.gender = it
        }
    )
}

@Composable
fun Step4(homeless: Homeless) {

    var description by remember { mutableStateOf(homeless.description) }

    OutlinedTextField(
        value = description,
        onValueChange = {
            description = it
            homeless.description = it
        },
        label = { Text("Descrizione") },
        maxLines = 10,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        ),
    )
}