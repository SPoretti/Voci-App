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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Volunteer
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.ProfileTextField
import com.example.vociapp.ui.components.SnackbarManager
import com.example.vociapp.ui.viewmodels.AuthResult
import com.google.firebase.auth.userProfileChangeRequest

@Composable
fun UpdateUserProfileScreen(navController: NavHostController) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.getAuthViewModel()
    val currentProfile = authViewModel.getCurrentUser()

    if(currentProfile != null){
        val loggedUserNickname = currentProfile.displayName
        Log.d(TAG, "Utente loggato (modifica): $loggedUserNickname")

        var nickname by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var surname by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        var showError by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }
        var isUpdating by remember { mutableStateOf(false) }
        var isNavigatingBack by remember { mutableStateOf(false) }
        var isInitialized by remember { mutableStateOf(false) }

        val volunteerViewModel = serviceLocator.getVolunteerViewModel()
        val volunteerLoggedEmail = authViewModel.getCurrentUser()?.email

        var loggedVolunteer by remember { mutableStateOf<Volunteer?>(null) }

        val volunteerResource by volunteerViewModel.getVolunteerByEmail(volunteerLoggedEmail)
            .collectAsState(initial = Resource.Loading())

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            IconButton(
                onClick = {
                    isNavigatingBack = true
                    navController.popBackStack()
                },
                enabled = !isNavigatingBack,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Profile",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Modifica il profilo",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
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
                        when (val resource = volunteerResource) {
                            is Resource.Loading -> CircularProgressIndicator()

                            is Resource.Error -> {
                                Text(
                                    text = "Errore: ${resource.message}",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            is Resource.Success -> {
                                val volunteer = resource.data
                                if (!isInitialized) {
                                    loggedVolunteer = volunteer
                                    nickname = volunteer?.nickname ?: ""
                                    name = volunteer?.name ?: ""
                                    surname = volunteer?.surname ?: ""
                                    isInitialized = true
                                }

                                    ProfileTextField(
                                        value = nickname,
                                        onValueChange = { nickname = it },
                                        label = "Nickname",
                                        icon = Icons.Default.Person,
                                        placeholder = "Nickname"
                                    )


                                ProfileTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = "Nome",
                                    icon = Icons.Default.Person,
                                    placeholder = "Nome"
                                )

                                ProfileTextField(
                                    value = surname,
                                    onValueChange = { surname = it },
                                    label = "Cognome",
                                    icon = Icons.Default.Person,
                                    placeholder = "Cognome"
                                )

                                ProfileTextField(
                                    value = password,
                                    onValueChange = { password = it },
                                    label = "Password",
                                    icon = Icons.Default.Password,
                                    placeholder = "Password"
                                )

                                val isPasswordValid = password == volunteer?.password && password.isNotBlank()

                                Button(
                                    onClick = { isUpdating = true },
                                    enabled = isPasswordValid && !isUpdating,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        if (isUpdating) "Aggiornamento in corso..." else "Conferma modifiche",
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
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

        LaunchedEffect(isUpdating) {
            if (isUpdating) {
                val result = authViewModel.updateUserProfile(nickname)
                if (result is AuthResult.Failure) {
                    showError = true
                    errorMessage = result.message
                } else {
                    showError = false
                    val updatedVolunteer = Volunteer("", name, surname, nickname)
                    Log.d(TAG, "Volontario aggiornato: $updatedVolunteer")
                    loggedVolunteer?.let { volunteerViewModel.updateVolunteer(it,updatedVolunteer) }
                    Log.d(TAG, "Volontario loggato dopo l'aggiornamento: $loggedVolunteer")
                    navController.popBackStack()
                }
                isUpdating = false
            }
        }
    }
}




