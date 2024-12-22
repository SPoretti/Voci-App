package com.example.vociapp.ui.screens.auth

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun SignUpScreen(
    navController: NavHostController
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    var isSigningUp by remember { mutableStateOf(false) }
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.getAuthViewModel()
    val volunteerViewModel = serviceLocator.getVolunteerViewModel()
    var showSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(isSigningUp) {
        if (isSigningUp) {
            val result = authViewModel.createUserWithEmailAndPassword(email, password)
            if (result is AuthResult.Failure) {
                errorMessage = result.message
                showSnackbar = true
            } else {
                val volunteer = Volunteer("", "", "", "", "", email)
                volunteerViewModel.addVolunteer(volunteer)
                authViewModel.sendVerificationEmail()
                navController.navigate(Screens.EmailVerification.route) {
                    popUpTo(Screens.SignUp.route) { inclusive = true }
                }
            }
            isSigningUp = false
        }
        if (showSnackbar) {
            SnackbarManager.showSnackbar(errorMessage)
            showSnackbar = false
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
//                        AuthTextField(
//                            value = name,
//                            onValueChange = { name = it },
//                            label = "Nome",
//                            icon = Icons.Default.PersonOutline
//                        )
//
//                        AuthTextField(
//                            value = surname,
//                            onValueChange = { surname = it },
//                            label = "Cognome",
//                            icon = Icons.Default.PersonOutline
//                        )
//
//                        AuthTextField(
//                            value = nickname,
//                            onValueChange = { nickname = it },
//                            label = "Nickname",
//                            icon = Icons.Default.PersonOutline
//                        )
//
//                        AuthTextField(
//                            value = phoneNumber,
//                            onValueChange = { phoneNumber = it },
//                            label = "Numero di telefono",
//                            icon = Icons.Default.Phone
//                        )

                            AuthTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = "Email",
                                icon = Icons.Default.Email
                            )

                            AuthTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = "Password",
                                icon = Icons.Default.Lock,
                                isPassword = true
                            )

                            AuthTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = "Conferma Password",
                                icon = Icons.Default.Lock,
                                isPassword = true
                            )

                            Button(
                                onClick = { isSigningUp = true },
                                enabled = !isSigningUp && password == confirmPassword,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                if (isSigningUp) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text("Registrati", modifier = Modifier.padding(vertical = 8.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = { navController.navigate("signIn") }
                    ) {
                        Text(
                            "Hai già un account? Accedi",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    )
}