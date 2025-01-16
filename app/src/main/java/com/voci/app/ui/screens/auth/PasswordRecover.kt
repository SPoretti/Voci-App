package com.voci.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.voci.app.di.LocalServiceLocator
import com.voci.app.ui.components.volunteers.AuthTextField
import com.voci.app.ui.components.volunteers.SnackbarManager
import com.voci.app.ui.components.volunteers.getTextFieldColors
import com.voci.app.ui.viewmodels.AuthResult
import kotlinx.coroutines.delay

@Composable
fun PasswordRecover(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.obtainAuthViewModel()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()

    var email by remember { mutableStateOf("") }
    var checkedEmail by remember { mutableStateOf(false) }
    var startTimer by remember { mutableStateOf(false) }
    var emailExists by remember { mutableStateOf(true) }

    LaunchedEffect(checkedEmail) {
        if (checkedEmail) {
            emailExists = volunteerViewModel.checkIfEmailExists(email)
            if (!emailExists) {
                SnackbarManager.showSnackbar("Email non registrata" )
            } else {
                val result = authViewModel.sendPasswordResetEmail(email)
                if (result is AuthResult.Success) {
                    startTimer = true
                    SnackbarManager.showSnackbar("Email inviata con successo, segui le indicazioni inviate via mail")
                } else if (result is AuthResult.Failure) {
                    SnackbarManager.showSnackbar(result.message)
                }
            }
        }
        emailExists = true
        checkedEmail = false
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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Recupero password",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Inserisci la tua email per il recupero della password",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(200.dp)
                            )

                            AuthTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = if (emailExists) "Email" else "Email non valida",
                                icon = Icons.Default.Email,
                                colors = getTextFieldColors(isValid = emailExists)
                            )

                            Button(
                                onClick = { checkedEmail = true },
                                enabled = email.isNotEmpty() && !startTimer && !checkedEmail,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                if (checkedEmail && emailExists && !startTimer) {
                                    Text("Invio email in corso...")

                                } else {
                                    Text(
                                        "Invia mail di recupero",
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }

                            if (startTimer) {
                                var delayTime by remember { mutableIntStateOf(30) }
                                Text(
                                    "Attendi $delayTime secondi per inviare un'altra email",
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

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = { navController.navigate("signIn") },
                    ) {
                        Text(
                            "Accedi",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        )
                    }
                }
            }
        }
    )
}