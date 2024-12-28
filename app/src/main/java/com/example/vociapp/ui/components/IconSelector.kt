package com.example.vociapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconSelector(
    onIconSelected: (IconCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIconCategory by remember { mutableStateOf(IconCategory.OTHER) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = selectedIconCategory.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Icona") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            IconCategory.entries.forEach { iconCategory ->
                DropdownMenuItem(
                    onClick = {
                        selectedIconCategory = iconCategory
                        expanded = false
                        onIconSelected(iconCategory)
                    },
                    text = { Text(iconCategory.name) },
                    leadingIcon = {
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