package com.example.vociapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dehaze
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.utils.hapticFeedback
import com.example.vociapp.ui.navigation.currentRoute

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    placeholderText: String,
    unfocusedBorderColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    navController: NavController,
    onLeadingIconClick: (() -> Unit)? = null
) {
    var searchText by remember { mutableStateOf("") }
    var isSearchBarFocused by remember { mutableStateOf(false) }
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.getAuthViewModel()
    val currentUser = authViewModel.getCurrentUserProfile()
    val currentRoute = currentRoute(navController)

    OutlinedTextField(
        value = searchText,
        onValueChange = { newText ->
            searchText = newText
            onSearch(newText)
        },
        modifier = modifier
            .clickable { onClick() }
            .onFocusChanged { focusState ->
                if (isSearchBarFocused && !focusState.isFocused) {
                    isSearchBarFocused = false
                    onDismiss()
                } else if (focusState.isFocused) {
                    isSearchBarFocused = true
                }
            },
        leadingIcon = {
            if (onLeadingIconClick != null){
                Row(
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    IconButton(
                        onClick = {
                            onLeadingIconClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Dehaze,
                            contentDescription = "Drawer",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(32.dp)
                        )
                    }
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(32.dp)
                )
            }

        },
        trailingIcon = {
            if(
                searchText.isNotEmpty()
                && isSearchBarFocused
            ) {
                Row(
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    IconButton(
                        onClick = {
                            searchText = ""
                            onSearch("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Account",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(32.dp)
                        )
                    }
                }
            } else {
                if(currentRoute == "home") {

                    if(currentUser?.photoUrl != null) {

                        Row(
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    navController.navigate("userProfile")
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .hapticFeedback()
                            ) {
                                AsyncImage(
                                    model = currentUser.photoUrl,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                    } else {
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
                    }
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