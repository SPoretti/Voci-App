package com.example.vociapp.ui.screens.auth

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.AuthTextField
import com.example.vociapp.ui.components.PasswordPopup
import com.example.vociapp.ui.components.SnackbarManager
import com.example.vociapp.ui.components.getTextFieldColors
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.viewmodels.AuthResult
import java.util.UUID

@Composable
fun SignUpScreen(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.obtainAuthViewModel()
    val user = authViewModel.getCurrentUser()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordFieldFocused by remember { mutableStateOf(false) }
    var isFieldEmpty by remember { mutableStateOf(true) }

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    var isSigningUp by remember { mutableStateOf(false) }
    var logging by remember { mutableStateOf(false) }
    var completingSignUp by remember { mutableStateOf(false) }

    var step by remember { mutableIntStateOf(1) }
    var emailVerified by remember { mutableStateOf(user?.isEmailVerified ?: false) }

    LaunchedEffect(isSigningUp) {
        if (isSigningUp) {
            if (authViewModel.areFieldsEmpty(email, password, confirmPassword)) {
                isFieldEmpty = false
                SnackbarManager.showSnackbar("Uno o più campi sono vuoti")
            } else {
                val isPasswordValid = authViewModel.isPasswordValid(password)
                if (isPasswordValid) {
                    val result = authViewModel.createUserWithEmailAndPassword(email, password)
                    if (result is AuthResult.Failure) {
                        SnackbarManager.showSnackbar(result.message)
                    } else {
                        step = 2
                        authViewModel.sendVerificationEmail()
                    }
                } else {
                    SnackbarManager.showSnackbar("Password non valida")
                }
            }
            isSigningUp = false
        }
    }

    LaunchedEffect(user) {
        while (true) {
            user?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emailVerified = user.isEmailVerified
                    Log.d("SignUpScreen", "Email verified: ${user.email}")
                }
            }
            kotlinx.coroutines.delay(3000)
        }
    }

    LaunchedEffect(emailVerified) {
        if (emailVerified) {
            SnackbarManager.showSnackbar("Email confermata!")
            step = 3;
        }
    }

    LaunchedEffect(completingSignUp) {
        if (completingSignUp) {
            if (authViewModel.areFieldsEmpty(name, surname, nickname, phoneNumber)) {
                SnackbarManager.showSnackbar("Compila tutti i campi")
                completingSignUp = false
                return@LaunchedEffect
            }

            val validPhoneNumber = authViewModel.isPhoneNumberValid(phoneNumber)
            if (!validPhoneNumber) {
                SnackbarManager.showSnackbar("Numero di telefono non valido")
                completingSignUp = false
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
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            completingSignUp = false
        }
    }

    //User is quitting before completing the registration
    BackHandler(onBack = {
        if (step == 2 || step == 3) {
            user?.delete()
        }
    })

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
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (step) {
                        // 1 -> Sign Up Screen
                        1 -> {
                            Text(
                                "Registrati",
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
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {

                                    AuthTextField(
                                        value = email,
                                        onValueChange = { email = it },
                                        label = "Email",
                                        icon = Icons.Default.Email,
                                        colors = getTextFieldColors(isValid = isFieldEmpty)
                                    )

                                    AuthTextField(
                                        value = password,
                                        onValueChange = { password = it },
                                        label = "Password",
                                        icon = Icons.Default.Lock,
                                        isPassword = !passwordVisible,
                                        trailingIcon = {
                                            IconButton(onClick = {
                                                passwordVisible = !passwordVisible
                                            }) {
                                                Icon(
                                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                    contentDescription = if (passwordVisible) "Nascondi password" else "Mostra password"
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .onFocusChanged { focusState ->
                                                Log.d("SignUpScreen", "Password focused: ${focusState.isFocused}")
                                                    isPasswordFieldFocused = focusState.isFocused
                                            },
                                        colors = getTextFieldColors(isValid = isFieldEmpty)
                                    )

                                    if (isPasswordFieldFocused) {
                                        PasswordPopup(
                                            onDismiss = {
                                                isPasswordFieldFocused = false
                                            }
                                        )
                                    }

                                    AuthTextField(
                                        value = confirmPassword,
                                        onValueChange = { confirmPassword = it },
                                        label = "Conferma Password",
                                        icon = Icons.Default.Lock,
                                        isPassword = true,
                                        colors = getTextFieldColors(isValid = isFieldEmpty)
                                    )

                                    Button(
                                        onClick = { isSigningUp = true },
                                        enabled = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            "Registrati",
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            TextButton(
                                onClick = { navController.navigate("signIn") }
                            ) {
                                Text(
                                    "Hai già un account? Accedi",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        // 2 -> Email Verification Screen
                        2 -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Verifica la mail",
                                    style = MaterialTheme.typography.headlineMedium,
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
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = "Email",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .size(78.dp)
                                                .align(Alignment.CenterHorizontally)
                                        )
                                        Text(
                                            "Ti abbiamo inviato un link di verifica al seguente indirizzo email: $email",
                                            style = MaterialTheme.typography.bodyMedium,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        // 3 -> Complete Registration Screen
                        3 -> {
                            Text(
                                "Crea un account",
                                style = MaterialTheme.typography.headlineMedium,
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
                                        label = "Telefono",
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
            }
        }
    )
}