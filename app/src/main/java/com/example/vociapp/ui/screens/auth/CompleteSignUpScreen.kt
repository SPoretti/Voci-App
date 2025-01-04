package com.example.vociapp.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.AuthTextField
import com.example.vociapp.ui.components.SnackbarManager
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.viewmodels.AuthResult
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun CompleteSignUpScreen(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.obtainAuthViewModel()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()
    val user = authViewModel.getCurrentUser()

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    var isSigningUp by remember { mutableStateOf(false) }
    var logging by remember { mutableStateOf(false) }

    LaunchedEffect(isSigningUp) {
        if (isSigningUp) {
            if (authViewModel.areFieldsEmpty(name, surname, nickname, phoneNumber)) {
                SnackbarManager.showSnackbar("Compila tutti i campi")
                isSigningUp = false
                return@LaunchedEffect
            }

            val validPhoneNumber = authViewModel.isPhoneNumberValid(phoneNumber)
            if (!validPhoneNumber) {
                SnackbarManager.showSnackbar("Numero di telefono non valido")
                isSigningUp = false
                return@LaunchedEffect
            } else {
                val result = authViewModel.updateUserProfile(nickname, null)
                if (result is AuthResult.Failure) {
                    SnackbarManager.showSnackbar(result.message)
                } else {
                    logging = true
                    val volunteer = Volunteer(
                        UUID.randomUUID().toString(),
                        name,
                        surname,
                        nickname,
                        phoneNumber,
                        user?.email ?: ""
                    )
                    volunteerViewModel.addVolunteer(volunteer)
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.CompleteSignUp.route) { inclusive = true }
                    }
                }
            }
            isSigningUp = false
        }
    }

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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Crea un account",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AuthTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = "Nome",
                                icon = Icons.Default.PersonOutline
                            )

                            AuthTextField(
                                value = surname,
                                onValueChange = { surname = it },
                                label = "Cognome",
                                icon = Icons.Default.PersonOutline
                            )

                            AuthTextField(
                                value = nickname,
                                onValueChange = { nickname = it },
                                label = "Nickname",
                                icon = Icons.Default.PersonOutline
                            )

                            AuthTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = "Numero di telefono",
                                icon = Icons.Default.Phone,
                                placeholder = "+39"
                            )

                            Button(
                                onClick = { isSigningUp = true },
                                enabled = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                if (logging) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text(
                                        "Completa Registrazione",
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}