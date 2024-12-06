package com.example.vociapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    var selectedGender by remember { mutableStateOf(Gender.Unspecified) }
    var isAddingHomeless by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Aggiungi SenzaTetto") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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

                //TODO(): birth date picker with fish's DatePicker
//        DatePicker()
//
//        Spacer(modifier = Modifier.height(16.dp))

                //Temporary
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
                    onValueChange = { homelessPets = it},
                    label = {Text("Animali")},
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
        }},
        confirmButton = {
            Button(
                onClick = {
                    isAddingHomeless = true
                    val newHomeless = Homeless(
                        name = homelessName,
                        gender = selectedGender,
                        location = homelessLocation,
                        pets = homelessPets,
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