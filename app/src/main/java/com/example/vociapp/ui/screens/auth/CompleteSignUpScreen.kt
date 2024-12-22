package com.example.vociapp.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Volunteer
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.AuthTextField
import com.example.vociapp.ui.components.SnackbarManager
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.viewmodels.AuthResult

@Composable
fun CompleteSignUpScreen(
    navController: NavHostController
){
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.getAuthViewModel()
    val volunteerViewModel = serviceLocator.getVolunteerViewModel()
    val user = authViewModel.getCurrentUser()

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    var isSigningUp by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(isSigningUp) {
        if (isSigningUp) {
            val result = authViewModel.updateUserProfile(nickname)
            Log.d("CompleteSignUpScreen", "Result: $result")
            if (result is AuthResult.Failure) {
                errorMessage = result.message
                showSnackbar = true
            } else {
                val updatedVolunteer = Volunteer("", name, surname, nickname, phoneNumber, user?.email ?: "")
                volunteerViewModel.completeVolunteerProfile(updatedVolunteer)
                Log.d("CompleteSignUpScreen", "Volunteer: $updatedVolunteer")
                navController.navigate(Screens.Home.route) {
                    popUpTo(Screens.CompleteSignUp.route) { inclusive = true }
                }
            }
            isSigningUp = false
        }
        if (showSnackbar) {
            SnackbarManager.showSnackbar(errorMessage)
            showSnackbar = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                            icon = Icons.Default.Phone
                        )

                    Button(
                        onClick = { isSigningUp = true },
                        enabled = !isSigningUp,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isSigningUp) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Completa Registrazione", modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}