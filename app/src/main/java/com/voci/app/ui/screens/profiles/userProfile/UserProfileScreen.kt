package com.voci.app.ui.screens.profiles.userProfile

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.voci.app.data.util.Resource
import com.voci.app.di.LocalServiceLocator
import com.voci.app.ui.components.core.SwipeDirection
import com.voci.app.ui.components.core.SwipeableScreen
import com.voci.app.ui.components.volunteers.ProfileInfoItem
import com.voci.app.ui.components.volunteers.SnackbarManager

@Composable
fun UserProfileScreen(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.obtainAuthViewModel()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()
    val currentProfile = authViewModel.getCurrentUser()
    val loggedUser = volunteerViewModel.getCurrentUser()

//    val currentVolunteer by volunteerViewModel.currentUser.collectAsState()

//    LaunchedEffect(currentVolunteer) {
//        if (currentVolunteer is Resource.Success) {
//            Log.d("Home", "Utente loggato: ${currentVolunteer.toString()}")
//        } else if (currentVolunteer is Resource.Error) {
//            Log.d("Home", "Errore utente: ${(currentVolunteer as Resource.Error).message}")
//            volunteerViewModel.fetchVolunteers()
//        }
//    }

    if (loggedUser != null) {
        volunteerViewModel.getVolunteerById(loggedUser.id)
    }

    val volunteerResource by volunteerViewModel.specificVolunteer.collectAsState()

    if (currentProfile != null) {
        Scaffold(
            snackbarHost = { SnackbarManager.CustomSnackbarHost() },
            content = { padding ->
                SwipeableScreen(
                    direction = SwipeDirection.RIGHT,
                    navController = navController,
                    targetRoute = "home"
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
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
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Transparent)
                                ) {

                                    // Edit button
                                    IconButton(
                                        onClick = { navController.navigate("UpdateUserProfile") },
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .size(40.dp),
                                        colors = IconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.primary,
                                            disabledContainerColor = MaterialTheme.colorScheme.secondary,
                                            disabledContentColor = MaterialTheme.colorScheme.onSecondary
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Profile",
                                            tint = MaterialTheme.colorScheme.onPrimary,
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
                                            .size(40.dp),
                                        colors = IconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.primary,
                                            disabledContainerColor = MaterialTheme.colorScheme.secondary,
                                            disabledContentColor = MaterialTheme.colorScheme.onSecondary
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                            contentDescription = "Logout",
                                            tint = MaterialTheme.colorScheme.onPrimary,
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
                                        when (volunteerResource) {

                                            is Resource.Loading -> {
                                                CircularProgressIndicator()
                                            }

                                            is Resource.Success -> {
                                                val volunteer = volunteerResource.data

                                                val initials =
                                                    "${volunteer?.name?.firstOrNull() ?: ""}${volunteer?.surname?.firstOrNull() ?: ""}".uppercase()

                                                //Profile picture
                                                if (currentProfile.photoUrl != null) {
                                                    AsyncImage(
                                                        model = currentProfile.photoUrl,
                                                        contentDescription = "Profile Picture",
                                                        modifier = Modifier
                                                            .size(120.dp)
                                                            .clip(CircleShape),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                } else {
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
                                                }

                                                Text(
                                                    text = volunteer!!.nickname,
                                                    style = MaterialTheme.typography.headlineMedium,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                HorizontalDivider(
                                                    modifier = Modifier.padding(
                                                        vertical = 8.dp
                                                    )
                                                )

                                                ProfileInfoItem(
                                                    icon = Icons.Default.Person,
                                                    label = "Volontario",
                                                    value = "${volunteer.name} ${volunteer.surname}",
                                                )

                                                // email
                                                ProfileInfoItem(
                                                    icon = Icons.Default.Email,
                                                    label = "Email",
                                                    value = volunteer.email
                                                )

                                                if (currentProfile.phoneNumber?.isNotEmpty() == true) {
                                                    // phone number
                                                    ProfileInfoItem(
                                                        icon = Icons.Default.Phone,
                                                        label = "Numero di telefono",
                                                        value = volunteer.phone_number
                                                    )
                                                } else {
                                                    ProfileInfoItem(
                                                        icon = Icons.Default.Phone,
                                                        label = "Numero di telefono",
                                                        value = volunteer.phone_number
                                                    )
                                                }
                                            }

                                            is Resource.Error -> {
                                                Text(
                                                    text = "Errore: ${volunteerResource.message}",
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
