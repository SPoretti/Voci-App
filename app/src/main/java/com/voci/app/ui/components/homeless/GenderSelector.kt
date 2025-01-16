package com.voci.app.ui.components.homeless

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.voci.app.data.util.Gender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelector(
    selectedGender: Gender?,
    onGenderSelected: (Gender) -> Unit
) {
    //----- Region: Data Initialization -----
    // Gender list
    val genders = Gender.entries
    // State variables
    var expanded by remember { mutableStateOf(false) }

    //----- Region: View Composition -----
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        // Outlined text field with dropdown menu anchored to it
        OutlinedTextField(
            readOnly = true,                            // Read only to prevent user from editing
            value = selectedGender?.displayName ?: "",  // Display the selected gender or an empty string
            onValueChange = { },                        // Empty onValueChange to prevent user from editing
            label = { Text("Sesso") },                  // Label for the text field
            trailingIcon = {                            // Trailing icon for the text field
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)   // Anchor to the text field
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(              // Colors for the text field
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
            ),
        )
        // Dropdown menu with the gender options
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            // Iterate over the gender options and create a dropdown menu item for each
            genders.forEach { selectionGender ->
                DropdownMenuItem(
                    text = { Text(selectionGender.displayName) },
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    onClick = {
                        onGenderSelected(selectionGender)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onBackground,
                    )
                )
            }
        }
    }
}
