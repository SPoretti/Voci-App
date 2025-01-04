package com.example.vociapp.ui.components.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vociapp.data.util.IconCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconSelector(
    onIconSelected: (IconCategory) -> Unit,     // Callback to handle icon selection
    selectedIconCategory: IconCategory,         // Currently selected icon category
    modifier: Modifier = Modifier,              // Modifier for styling
) {
    //----- Region: Data Initialization -----
    // Value used to display the dropdown menu
    var expanded by remember { mutableStateOf(false) }

    // Dropdown menu to select the icon category
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        // OutlinedTextField used to display the currently selected icon category
        OutlinedTextField(
            readOnly = true,                                // Only display the text
            value = selectedIconCategory.displayName,       // Display the currently selected icon
            onValueChange = { },                            // Do nothing when the text is changed (necessary default field)
            label = { Text("Icona") },                      // Label for the text field
            trailingIcon = {
                // Display the dropdown icon based on the expanded state
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)   // Anchor the dropdown menu to the text field
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(      // Style
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
            ),
            leadingIcon = {
                // Display the currently selected icon
                Icon(
                    painter = painterResource(id = iconCategoryMap[selectedIconCategory]!!),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
        // Dropdown menu to display the list of icon categories
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            // Display each icon category as a dropdown item
            IconCategory.entries.forEach { iconCategory ->
                DropdownMenuItem(
                    text = { Text(iconCategory.displayName) }, // Display the icon category name
                    onClick = {
                        // Callback
                        onIconSelected(iconCategory)
                        // Close the dropdown
                        expanded = false
                    },
                    // Style
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    leadingIcon = {
                        // Display the icon for the icon category
                        Icon(
                            painter = painterResource(id = iconCategoryMap[iconCategory]!!),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
            }
        }
    }
}