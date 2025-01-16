package com.voci.app.ui.screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.voci.app.R
import com.voci.app.data.local.database.Volunteer
import com.voci.app.di.LocalServiceLocator
import com.voci.app.ui.components.volunteers.AuthTextField
import com.voci.app.ui.components.volunteers.PasswordPopup
import com.voci.app.ui.components.volunteers.SnackbarManager
import com.voci.app.ui.components.volunteers.getTextFieldColors
import com.voci.app.ui.viewmodels.AuthResult
import java.util.UUID

@Composable
fun SignUpScreen(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.obtainAuthViewModel()
    val user = authViewModel.getCurrentUser()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()
    val networkManager = serviceLocator.obtainNetworkManager()
    val isConnected = networkManager.isNetworkConnected()

    //Step 1
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordFieldFocused by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }

    var fieldEmpty by remember { mutableStateOf(false) }
    var nicknameAlreadyUsed by remember { mutableStateOf(false) }
    var validPhoneNumber by remember { mutableStateOf(true) }

    //Step 2
    var emailVerified by remember { mutableStateOf(user?.isEmailVerified) }

    //Step 3
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    var isSigningUp by remember { mutableStateOf(false) }
    var logging by remember { mutableStateOf(true) }
    var step by remember { mutableIntStateOf(1) }

    LaunchedEffect(isSigningUp) {
        if (!isConnected) {
            SnackbarManager.showSnackbar("Nessuna connessione ad internet")
            isSigningUp = false
            return@LaunchedEffect
        }
        if (isSigningUp) {
            when (step) {
                1 -> {
                    if (authViewModel.areFieldsEmpty(email, password, confirmPassword)) {
                        logging = false
                        SnackbarManager.showSnackbar("Uno o più campi sono vuoti")
                        isSigningUp = false
                        return@LaunchedEffect
                    }

                    isPasswordValid = authViewModel.isPasswordValid(password)
                    isEmailValid = authViewModel.isEmailValid(email)

                    if (!(isPasswordValid && isEmailValid)) {
                        SnackbarManager.showSnackbar("Email o password non validi")
                        isSigningUp = false
                        return@LaunchedEffect
                    }

                    val result = authViewModel.createUserWithEmailAndPassword(email, password)
                    if (result is AuthResult.Failure) {
                        SnackbarManager.showSnackbar(result.message)
                    } else {
                        step = 2
                        authViewModel.sendVerificationEmail()
                    }
                }

                3 -> {
                    fieldEmpty = authViewModel.areFieldsEmpty(name, surname, nickname, phoneNumber)
                    if (fieldEmpty) {
                        logging = false
                        SnackbarManager.showSnackbar("Compila tutti i campi")
                        isSigningUp = false
                        return@LaunchedEffect
                    }

                    nicknameAlreadyUsed = volunteerViewModel.checkIfNicknameExists(nickname)
                    if (nicknameAlreadyUsed) {
                        nicknameAlreadyUsed = false
                        logging = false
                        SnackbarManager.showSnackbar("Nickname già utilizzato")
                        isSigningUp = false
                        return@LaunchedEffect
                    }

                    validPhoneNumber = authViewModel.isPhoneNumberValid(phoneNumber)
                    if (!validPhoneNumber) {
                        validPhoneNumber = true
                        logging = false
                        SnackbarManager.showSnackbar("Numero di telefono non valido")
                        isSigningUp = false
                        return@LaunchedEffect
                    }

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
                        navController.navigate("home")
                    }
                }
            }
        }
        isPasswordValid = true
        isEmailValid = true
        logging = true
        isSigningUp = false
    }

    LaunchedEffect(user) {
        while (true) {
            user?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emailVerified = user.isEmailVerified
                }
            }
            kotlinx.coroutines.delay(3000)
        }
    }

    LaunchedEffect(emailVerified) {
        if (emailVerified == true) {
            SnackbarManager.showSnackbar("Email confermata!")
            step = 3
        }
    }

    //User is quitting before completing the registration
    BackHandler(onBack = {
        if (step == 1) {
            navController.navigate("signIn")
        }
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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (step) {
                        // 1 -> Sign Up Screen
                        1 -> {
                            Image(
                                painter = painterResource(id = R.drawable.voci_logo),
                                contentDescription = "Logo",
                                modifier = Modifier.height(128.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
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
                                        isLoggingIn = logging,
                                        colors = getTextFieldColors(isValid = isEmailValid)
                                    )

                                    AuthTextField(
                                        value = password,
                                        onValueChange = { password = it },
                                        label = "Password",
                                        icon = Icons.Default.Lock,
                                        isLoggingIn = logging,
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
                                                isPasswordFieldFocused = focusState.isFocused
                                            },
                                        colors = getTextFieldColors(isValid = isPasswordValid)
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
                                        isLoggingIn = logging,
                                        isPassword = true,
                                        colors = getTextFieldColors(isValid = isPasswordValid)
                                    )

                                    Button(
                                        onClick = { isSigningUp = true; logging = false },
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

                            Spacer(modifier = Modifier.height(8.dp))

                            TextButton(
                                onClick = { navController.navigate("signIn") }
                            ) {
                                Text(
                                    "Hai già un account? Accedi",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
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
                                            "Ti abbiamo inviato un link di verifica al seguente indirizzo email: ${user?.email}",
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
                                "Registrati",
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
                                        icon = Icons.Default.PersonOutline,
                                        isLoggingIn = logging,
                                    )

                                    AuthTextField(
                                        value = surname,
                                        onValueChange = { surname = it },
                                        label = "Cognome",
                                        icon = Icons.Default.PersonOutline,
                                        isLoggingIn = logging,
                                    )

                                    AuthTextField(
                                        value = nickname,
                                        onValueChange = { nickname = it },
                                        label = if (nicknameAlreadyUsed) "Nickname già utilizzato" else "Nickname",
                                        icon = Icons.Default.PersonOutline,
                                        isLoggingIn = logging,
                                        colors = getTextFieldColors(isValid = !nicknameAlreadyUsed)
                                    )

                                    AuthTextField(
                                        value = phoneNumber,
                                        onValueChange = { phoneNumber = it },
                                        label = "Telefono",
                                        icon = Icons.Default.Phone,
                                        isLoggingIn = logging,
                                        placeholder = "+39",
                                        colors = getTextFieldColors(isValid = validPhoneNumber)
                                    )

                                    Button(
                                        onClick = { isSigningUp = true; logging = false },
                                        enabled = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
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
    )
}