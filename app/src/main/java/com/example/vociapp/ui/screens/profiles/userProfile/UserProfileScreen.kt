package com.example.vociapp.ui.screens.profiles.userProfile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.ProfileInfoItem
import com.example.vociapp.ui.navigation.Screens

@Composable
fun UserProfileScreen(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.getAuthViewModel()
    val volunteerViewModel = serviceLocator.getVolunteerViewModel()
    val currentProfile = authViewModel.getCurrentUser()

    if(currentProfile != null){
        val loggedUserNickname = currentProfile.displayName
        Log.d(TAG, "Utente loggato (profilo utente): $loggedUserNickname")
        val volunteerLoggedEmail = authViewModel.getCurrentUser()?.email
        val volunteerResource by volunteerViewModel.getVolunteerByEmail(volunteerLoggedEmail)
            .collectAsState(initial = Resource.Loading())
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().background(Color.Transparent)) {

                        // Edit button
                        IconButton(
                            onClick = { navController.navigate(Screens.UpdateUserProfile.route) },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .size(38.dp),
                            colors = IconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                                disabledContentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clip(CircleShape)
                                    .size(40.dp),
                            )
                        }

                        // Logout button
                        IconButton(
                            onClick = { authViewModel.signOut() },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(38.dp),
                            colors = IconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                                disabledContentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clip(CircleShape)
                                    .size(40.dp)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            when (val resource = volunteerResource) {

                                is Resource.Loading -> {
                                    CircularProgressIndicator()
                                }

                                is Resource.Error -> {
                                    Text(
                                        text = "Errore: ${resource.message}",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }

                                is Resource.Success -> {
                                    val volunteer = resource.data
                                    // Profile picture placeholder
//                                    Box(
//                                        modifier = Modifier
//                                            .size(120.dp)
//                                            .clip(CircleShape)
//                                            .background(MaterialTheme.colorScheme.surface),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Default.Person,
//                                            contentDescription = "Profile Picture",
//                                            modifier = Modifier.size(64.dp),
//                                            tint = MaterialTheme.colorScheme.primary
//                                        )
//                                    }

                                    val initials = "${volunteer?.name?.firstOrNull() ?: ""}${volunteer?.surname?.firstOrNull() ?: ""}".uppercase()

                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(130.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    ) {
                                        Text(
                                            text = initials,
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 60.sp
                                        )
                                    }

                                    Text(
                                        text = volunteer?.nickname ?: "Unknown Volunteer",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                                        ProfileInfoItem(
                                            icon = Icons.Default.Person,
                                            label = "Volontario",
                                            value = "${volunteer?.name ?: "Unknown Volunteer"} ${volunteer?.surname ?: "Unknown Volunteer"}",
                                        )

                                    // email
                                    ProfileInfoItem(
                                        icon = Icons.Default.Email,
                                        label = "Email",
                                        value = volunteer?.email ?: "Unknown Volunteer"
                                    )

                                    // phone number
                                    ProfileInfoItem(
                                        icon = Icons.Default.Phone,
                                        label = "Phone Number",
                                        value = volunteer?.phoneNumber ?: "Unknown Volunteer"
                                    )

                                    // Edit Profile Section
                                    Button(
                                        onClick =
                                        { navController.navigate(Screens.UpdateUserProfile.route) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Modifica profilo")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Errore: Nessun utente loggato.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }


}