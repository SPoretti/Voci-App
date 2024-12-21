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
import com.example.vociapp.ui.components.SnackbarManager
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.AuthTextField
import com.example.vociapp.ui.components.keyboardAsState
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.viewmodels.AuthResult

@Composable
fun SignInScreen(
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isSigningIn by remember { mutableStateOf(false) }
    val serviceLocator = LocalServiceLocator.current
    val volunteerViewModel = serviceLocator.getVolunteerViewModel()
    val authViewModel = serviceLocator.getAuthViewModel()

    LaunchedEffect(isSigningIn) {
        if (isSigningIn) {
            try {
                showError = false
                errorMessage = ""

                if (password.isEmpty() || email.isEmpty()) {
                    showError = true
                    errorMessage = "Compila entrambi i campi"
                    return@LaunchedEffect
                } else {
                    val result = authViewModel.signInWithEmailAndPassword(email, password)
                    if (result is AuthResult.Failure) {
                        showError = true
                        errorMessage = "Email o password errate"
                    } else {
                        volunteerViewModel.getVolunteerByEmail(email)
                        navController.navigate(Screens.Home.route) {
                            popUpTo(Screens.SignIn.route) { inclusive = true }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("AuthFlow", "Errore imprevisto: ${e.localizedMessage}")
                showError = true
                errorMessage = "Errore imprevisto: ${e.localizedMessage}"
            } finally {
                isSigningIn = false
            }
        }
    }

    if (showError) {
        LaunchedEffect(errorMessage.isNotEmpty()) {
            SnackbarManager.showSnackbar(errorMessage)
            showError = false
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
                        "Accedi",
                        style = MaterialTheme.typography.headlineLarge,
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
                            Text(
                                text = "Inserisci email e password",
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

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

                    Button(
                        onClick = { isSigningIn = true },
                        enabled = !isSigningIn,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isSigningIn) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Accedi", modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }

                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = { navController.navigate("signUp") }
                    ) {
                        Text("Non hai un account? Registrati!", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    )
}


