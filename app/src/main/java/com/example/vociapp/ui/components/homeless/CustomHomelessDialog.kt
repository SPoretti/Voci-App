package com.example.vociapp.ui.components.homeless

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
import com.example.vociapp.ui.components.core.ConfirmButton
import com.example.vociapp.ui.components.core.DismissButton
import com.example.vociapp.ui.components.maps.SearchBox

@Composable
fun CustomHomelessDialog(
    onDismiss: () -> Unit,              // Callback to dismiss the dialog
    onConfirm: (Homeless) -> Unit,      // Callback to add (or modify) the homeless to the database
    homeless: Homeless = Homeless(),    // Homeless object to modify or add
    actionText: String                  // Text to display on the confirm button e.g. "Aggiungi" or "Modifica"
) {
    //----- Region: Data Initialization -----
    // Error variables
    var nameError by remember { mutableStateOf<String?>(null) }
    // Step variables
    var currentStep by remember { mutableIntStateOf(1) }
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
        title = { Text("$actionText persona") },
        // Main Content
        text = {
            // Display based on step state
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                when (currentStep) {
                    // Step 1: Name and location (the only two required fields)
                    1 -> Step1(
                        homeless = homeless,
                        nameError = nameError,
                        onNameErrorChange = { nameError = it }
                    )
                    // Step 2: Location
                    2 -> Step2(
                        onConfirmLocation = {
                            homeless.location = it
                            currentStep++
                        }
                    )
                    // Step 3: Age, nationality
                    3 -> Step3(homeless)
                    // Step 4: Pets, gender
                    4 -> Step4(homeless)
                    // Step 5: Description
                    5 -> Step5(homeless)
                }
            }
        },
        // Action Buttons
        confirmButton = {
            // Confirm button based on step state
            when (currentStep) {
                1 -> {
                    // If the current step is the first step, next button works if name is not empty
                    ConfirmButton(
                        onClick = {
                            if (homeless.name.isEmpty()){
                                nameError = "Il nome è obbligatorio"
                            }
                            else{
                                currentStep++
                            }
                        },
                        text = "Avanti",
                    )
                }
                2 -> {
                    // No need for a confirm button in this step since it moves on the next step
                    // by clicking on Confirm Location FAB
                }
                5 -> {
                    // If the current step is the last step, show the final button
                    ConfirmButton(
                        onClick = {
                            isAddingHomeless = true
                            onConfirm(homeless)
                        },
                        text = actionText
                    )
                }
                else -> {
                    // If the current step is the third or fourth step, show the next button
                    ConfirmButton(
                        onClick = { currentStep++ },
                        text = "Avanti"
                    )
                }

            }
        },
        dismissButton = {
            // Dismiss button based on step state
            if (currentStep > 1) {
                // If the current step is not the first step, show the back button
                DismissButton(
                    onClick = { currentStep-- },
                    text = "Indietro"
                )
            } else {
                // If the current step is the first step, show the dismiss button
                DismissButton(
                    onClick = { onDismiss() },
                )
            }
        }
    )
}

//----------------------------------- Region: Step Functions -----------------------------------

// Step 1: Name - required field
@Composable
fun Step1(
    homeless: Homeless,                     // Homeless object to modify
    nameError: String? = null,              // Error message to display if name is empty
    onNameErrorChange: (String?) -> Unit,   // Callback to change the name error
) {
    //----- Region: Data Initialization -----
    var name by remember { mutableStateOf(homeless.name) }

    //----- Region: View Composition -----
    // Name
    OutlinedTextField(
        value = name,
        onValueChange = {
            name = it
            homeless.name = it
            onNameErrorChange(null)
        },
        label = { Text("Nome") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        ),
        isError = (nameError != null)    // Show error if name is empty
    )

    if (nameError != null){              // Show error message if not null
        Text(nameError, color = Color.Red)
    }
    else                                 // Else add a spacer
        Spacer(modifier = Modifier.height(20.dp))
}

// Step 2: Location - required field
@Composable
fun Step2(
    onConfirmLocation: (String) -> Unit,
) {
    //----- Region: View Composition -----
    SearchBox(
        onConfirmLocation = { onConfirmLocation(it) },
    )
}

// Step 3: Age, nationality
@Composable
fun Step3(homeless: Homeless) {
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

// Step 4: Pets, gender
@Composable
fun Step4(homeless: Homeless) {
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

// Step 5: Description
@Composable
fun Step5(homeless: Homeless) {
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