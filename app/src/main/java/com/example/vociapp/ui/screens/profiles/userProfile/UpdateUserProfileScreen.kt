package com.example.vociapp.ui.screens.profiles.userProfile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.ProfilePictureDialog
import com.example.vociapp.ui.components.ProfileTextField
import com.example.vociapp.ui.components.SnackbarManager
import com.example.vociapp.ui.viewmodels.AuthResult

@Composable
fun UpdateUserProfileScreen(
    navController: NavHostController,
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.obtainAuthViewModel()
    val currentProfile = authViewModel.getCurrentUser()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()

    val loggedUser = volunteerViewModel.getCurrentUser()
    if (loggedUser != null) {
        volunteerViewModel.getVolunteerById(loggedUser.id)
    }

    val volunteerResource by volunteerViewModel.specificVolunteer.collectAsState()

    if (currentProfile != null) {
        var photoUrl by remember { mutableStateOf("") }
        var nickname by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var surname by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        var showError by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }
        var isUpdating by remember { mutableStateOf(false) }
        var isInitialized by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }
        var isPasswordCorrect by remember { mutableStateOf(true) }

        Scaffold(
            snackbarHost = { SnackbarManager.CustomSnackbarHost() },
            content = { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Modifica il profilo",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                when (volunteerResource) {
                                    is Resource.Loading -> CircularProgressIndicator()

                                    is Resource.Success -> {
                                        val volunteer = volunteerResource.data

                                        if (!isInitialized) {
                                            nickname = volunteer?.nickname ?: ""
                                            name = volunteer?.name ?: ""
                                            surname = volunteer?.surname ?: ""
                                            isInitialized = true
                                        }

                                        Row (
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box (
                                                modifier = Modifier.padding(end = 16.dp)
                                            ) {
                                                AsyncImage(
                                                    model = currentProfile.photoUrl,
                                                    contentDescription = "Profile Picture",
                                                    modifier = Modifier
                                                        .size(120.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop
                                                )

                                                IconButton(
                                                    onClick = { showDialog = true },
                                                    modifier = Modifier
                                                        .align(Alignment.BottomEnd)
                                                        .size(32.dp)
                                                        .clip(CircleShape)
                                                        .background(MaterialTheme.colorScheme.primary)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Modifica Profilo",
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }

                                                ProfilePictureDialog(
                                                    showDialog = showDialog,
                                                    onDismiss = { showDialog = false },
                                                    onSave = { newPhotoUrl ->
                                                        photoUrl = newPhotoUrl
                                                        showDialog = false
                                                    },
                                                    initialPhotoUrl = photoUrl
                                                )

                                            }

                                            Column (
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                ProfileTextField(
                                                    value = name,
                                                    onValueChange = { name = it },
                                                    label = "Nome",
                                                    placeholder = "Nome"
                                                )
                                                ProfileTextField(
                                                    value = surname,
                                                    onValueChange = { surname = it },
                                                    label = "Cognome",
                                                    placeholder = "Cognome"
                                                )
                                            }
                                        }

                                        ProfileTextField(
                                            value = nickname,
                                            onValueChange = { nickname = it },
                                            label = "Nickname",
                                            placeholder = "Nickname"
                                        )

                                        ProfileTextField(
                                            value = password,
                                            onValueChange = { password = it },
                                            label = if (isPasswordCorrect) "Password" else "Password errata",
                                            placeholder = "Password",
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = if (isPasswordCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                                unfocusedBorderColor = if (isPasswordCorrect) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f) else MaterialTheme.colorScheme.error,
                                                cursorColor = if (isPasswordCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                                focusedLabelColor = if (isPasswordCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                            )
                                        )

                                        Row (
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            OutlinedButton(
                                                onClick = { navController.popBackStack() },
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    containerColor = Color.Transparent,
                                                    contentColor = MaterialTheme.colorScheme.onBackground,
                                                ),
                                                border = BorderStroke(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.onBackground
                                                )
                                            ) {
                                                Text("Annulla")
                                            }

                                            Button(
                                                onClick = { isUpdating = true },
                                                enabled = true,
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary,
                                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                                ),
                                                border = BorderStroke(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.primary
                                                ),

                                                ) {
                                                Text(
                                                    "Conferma"
                                                )
                                            }
                                        }

                                        LaunchedEffect(isUpdating) {
                                            if (isUpdating) {
                                                var result =
                                                    authViewModel.signInWithEmailAndPassword(
                                                        volunteer!!.email,
                                                        password
                                                    )
                                                if (result is AuthResult.Failure) {
                                                    isPasswordCorrect = false
                                                    //SnackbarManager.showSnackbar(result.message)
                                                } else {
                                                    result = authViewModel.updateUserProfile(
                                                        nickname,
                                                        photoUrl
                                                    )
                                                    if (result is AuthResult.Failure) {
                                                        showError = true
                                                        errorMessage = result.message
                                                    } else {
                                                        showError = false
                                                        volunteer.name = name
                                                        volunteer.surname = surname
                                                        volunteer.nickname = nickname
                                                        volunteerViewModel.updateVolunteer(
                                                            volunteer
                                                        )
                                                        navController.popBackStack()
                                                    }
                                                }
                                                isUpdating = false
                                            }
                                        }
                                    }

                                    is Resource.Error -> {
                                        Text(
                                            text = "Errore: ${volunteerResource.message}",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }

                                if (showError) {
                                    Text(
                                        errorMessage,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}




