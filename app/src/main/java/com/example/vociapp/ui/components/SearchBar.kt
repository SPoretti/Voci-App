package com.example.vociapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vociapp.ui.components.utils.hapticFeedback

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    placeholderText: String,
    unfocusedBorderColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }
    var isSearchBarFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = searchText,
        onValueChange = { newText ->
            searchText = newText
            onSearch(newText) // Trigger search when text changes
        },
        modifier = modifier
            .clickable { onClick() }
            .onFocusChanged { focusState ->
                if (isSearchBarFocused && !focusState.isFocused) {
                    // Focus was lost
                    isSearchBarFocused = false

                    // Perform actions here, e.g., hide keyboard, clear search
                    // Example:
                    onDismiss()
                } else if (focusState.isFocused) {
                    // Gained focus
                    isSearchBarFocused = true
                }
            },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            Row(
                modifier = Modifier
                    .padding(end = 8.dp)
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("userProfile")
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .hapticFeedback()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Account",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
            }
        },
        placeholder = { Text(placeholderText) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = unfocusedBorderColor,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(35.dp)
    )
}