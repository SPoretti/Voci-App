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
import com.example.vociapp.ui.components.AuthTextField
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.viewmodels.AuthResult
import com.example.vociapp.ui.viewmodels.AuthViewModel

@Composable
fun SignInScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isSigningIn by remember { mutableStateOf(false) }

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
                "Sign In",
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
                            Text("Sign In", modifier = Modifier.padding(vertical = 8.dp))
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

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = { navController.navigate("signUp") }
            ) {
                Text("Don't have an account? Sign Up", color = MaterialTheme.colorScheme.primary)
            }
        }
    }

    LaunchedEffect(isSigningIn) {
        if (isSigningIn) {
            val result = authViewModel.signInWithEmailAndPassword(email, password)
            if (result is AuthResult.Failure) {
                showError = true
                errorMessage = result.message
            } else {
                navController.navigate(Screens.Home.route) {
                    popUpTo(Screens.SignIn.route) { inclusive = true }
                }
            }
            isSigningIn = false
        }
    }
}
