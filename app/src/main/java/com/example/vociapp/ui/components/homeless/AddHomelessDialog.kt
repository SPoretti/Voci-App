package com.example.vociapp.ui.components.homeless

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.vociapp.data.local.database.Homeless

@Composable
fun AddHomelessDialog(
    onDismiss: () -> Unit,      // Callback to dismiss the dialog
    onAdd: (Homeless) -> Unit   // Callback to add the homeless to the database
) {
    //----- Region: Data Initialization -----
    // Error variables
    var nameError by remember { mutableStateOf<String?>(null) }
    var locationError by remember { mutableStateOf<String?>(null) }
    // Step variables
    var currentStep by remember { mutableIntStateOf(1) }
    val homeless = remember { Homeless() }
    // Action Variable
    var isAddingHomeless by remember { mutableStateOf(false) }

    //----- Region: View Composition -----
    AlertDialog(
        // Called when the user tries to dismiss the Dialog by pressing the back button.
        // This is not called when the dismiss button is clicked.
        onDismissRequest = { onDismiss() },
        // Style
        properties = DialogProperties( usePlatformDefaultWidth = false ),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground,
        // Tile
        title = { Text("Aggiungi Senzatetto") },
        // Main Content
        text = {
            // Display based on step state
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                when (currentStep) {
                    // Step 1: Name and location (the only two required fields)
                    1 -> Step1(homeless, nameError, locationError) { name, location ->
                        nameError = if (name.isBlank()) "Inserire il nome" else null
                        locationError = if (location.isBlank()) "Inserire il luogo" else null
                    }
                    // Step 2: Age, nationality
                    2 -> Step2(homeless)
                    // Step 3: Pets, gender
                    3 -> Step3(homeless)
                    // Step 4: Description
                    4 -> Step4(homeless)
                }
            }
        },
        // Action Buttons
        confirmButton = {
            // Confirm button based on step state
            if (currentStep < 4) {
                // If the current step is not the last step, show the next button
                Button(
                    onClick = {
                        if (currentStep == 1) {
                            // Field validation for step 1
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
                // If the current step is the last step, show the add button
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
            // Dismiss button based on step state
            if (currentStep > 1) {
                // If the current step is not the first step, show the back button
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
                // If the current step is the first step, show the dismiss button
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
        }
    )
}

//----------------------------------- Region: Step Functions -----------------------------------

// Step 1: Name and location (the only two required fields)
@Composable
fun Step1(
    homeless: Homeless,                         // Homeless object to modify
    nameError: String?,                         // Error message for the name field
    locationError: String?,                     // Error message for the location field
    onFieldsChanged: (String, String) -> Unit   // Callback to update the fields
) {
    //----- Region: Data Initialization -----

    var name by remember { mutableStateOf(homeless.name) }
    var location by remember { mutableStateOf(homeless.location) }

    //----- Region: View Composition -----
    // Name
    OutlinedTextField(
        value = name,
        onValueChange = {
            name = it
            homeless.name = it
            onFieldsChanged(it, location)
        },
        label = { Text("Nome") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        ),
        isError = nameError != null,    // Show error if not null
    )

    if (nameError != null) {            // Show error message if not null
        Text(nameError, color = Color.Red)
    }
    else                                // Else add a spacer
        Spacer(modifier = Modifier.height(20.dp))
    // Location
    OutlinedTextField(
        value = location,
        onValueChange = {
            location = it
            homeless.location = it
            onFieldsChanged(name, it)
        },
        label = { Text("Luogo") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        ),
        isError = locationError != null,    // Show error if not null
    )
    if (locationError != null) {            // Show error message if not null
        Text(locationError, color = Color.Red)
    }
    else                                    // Else add a spacer
        Spacer(modifier = Modifier.height(20.dp))
}

// Step 2: Age, nationality
@Composable
fun Step2(homeless: Homeless) {
    //----- Region: Data Initialization -----

    var age by remember { mutableStateOf(homeless.age) }
    var nationality by remember { mutableStateOf(homeless.nationality) }

    //----- Region: View Composition -----
    // Age
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
    // Nationality
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

// Step 3: Pets, gender
@Composable
fun Step3(homeless: Homeless) {
    //----- Region: Data Initialization -----

    var pets by remember { mutableStateOf(homeless.pets) }
    var gender by remember { mutableStateOf(homeless.gender) }

    //----- Region: View Composition -----
    // Pets
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
    // Gender
    GenderSelector(
        selectedGender = gender,
        onGenderSelected = {
            gender = it
            homeless.gender = it
        }
    )
}

// Step 4: Description
@Composable
fun Step4(homeless: Homeless) {
    //----- Region: Data Initialization -----

    var description by remember { mutableStateOf(homeless.description) }

    //----- Region: View Composition -----
    // Description
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