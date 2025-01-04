package com.example.vociapp.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.utils.hapticFeedback

@Composable
fun SearchBar(
    navController: NavController,   // Navigation controller for navigation
    onLeadingIconClick: () -> Unit  // Callback to open the drawer
) {
    //----- Region: Data Initialization -----
    var searchText by remember { mutableStateOf("") }
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val authViewModel = serviceLocator.obtainAuthViewModel()
    val currentUser = authViewModel.getCurrentUserProfile()

    //----- Region: View Composition -----
    OutlinedTextField(
        // Displayed text
        value = searchText,
        // On value change
        onValueChange = { newText ->
            searchText = newText                                // Update displayed text
            homelessViewModel.updateSearchQuery(searchText)     // Update search query
        },
        // Style
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        placeholder = {
            Text(
                text = "Cerca...",
            )
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = Color.Transparent,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(35.dp),
        // Leading icon to open the drawer in the Home Page
        leadingIcon = {
            Row(
                modifier = Modifier
                    .padding(start = 8.dp)
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
        },
        // Trailing icon has 3 possible states
        // 1 -> X to clear search
        // 2 -> Photo from url
        // 3 -> Person icon
        trailingIcon = {
            if( searchText.isNotEmpty() ) { // If the search query is not empty show an X to clear it
                Row(
                    modifier = Modifier
                        .padding(end = 12.dp)
                ) {
                    IconButton(
                        onClick = {
                            searchText = ""
                            homelessViewModel.updateSearchQuery(searchText)
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
            } else { // If the search query is empty show the user icon to navigate to the profile
                if(currentUser?.photoUrl != null) { // If the user has a photoUrl set use it
                    Row(
                        modifier = Modifier.padding(end = 12.dp)
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
                            // Async Image using the photoUrl
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
                } else { // If the user has no photoUrl set use the default icon
                    Row(
                        modifier = Modifier
                            .padding(end = 12.dp)
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
    )
}