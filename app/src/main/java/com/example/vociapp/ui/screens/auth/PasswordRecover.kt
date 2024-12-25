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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.AuthTextField
import com.example.vociapp.ui.components.SnackbarManager
import com.example.vociapp.ui.viewmodels.AuthResult
import kotlinx.coroutines.delay

@Composable
fun PasswordRecover(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.getAuthViewModel()

    var email by remember { mutableStateOf("") }
    var checkedEmail by remember { mutableStateOf(false) }
    var startTimer by remember { mutableStateOf(false) }

    LaunchedEffect(checkedEmail) {
        if (checkedEmail) {
            if (email.isEmpty()) {
                SnackbarManager.showSnackbar("Compila il campo email")
            } else {
                val result = authViewModel.sendPasswordResetEmail(email)
                if (result is AuthResult.Success) {
                    SnackbarManager.showSnackbar("Email inviata con successo")
                    startTimer = true
                } else if (result is AuthResult.Failure) {
                    SnackbarManager.showSnackbar(result.message)
                }
                //navController.navigate("signIn")
            }
            checkedEmail = false
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
                        "Recupero password",
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
                                "Inserisci la tua email per il recupero della password",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            AuthTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = "Email",
                                icon = Icons.Default.Email
                            )

                            Button(
                                onClick = { checkedEmail = true },
                                enabled = email.isNotEmpty() && !startTimer && !checkedEmail,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                if (checkedEmail && email.isNotEmpty() && !startTimer) {
                                    Text("Invio email in corso...")

                                } else {
                                    Text(
                                        "Invia mail di recupero",
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }

                            if (startTimer) {
                                var delayTime by remember { mutableStateOf(30) }
                                Text(
                                    "Attenti $delayTime secondi per inviare un'altra email",
                                    textAlign = TextAlign.Center
                                )

                                LaunchedEffect(key1 = delayTime) {
                                    while (delayTime > 0) {
                                        delay(1000)
                                        delayTime -= 1
                                    }
                                    startTimer = false
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = { navController.navigate("signIn") },
                    ) {
                        Text(
                            "Accedi",
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    )
}