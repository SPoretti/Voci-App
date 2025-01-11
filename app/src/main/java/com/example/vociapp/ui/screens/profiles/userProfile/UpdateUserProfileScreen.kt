package com.example.vociapp.ui.screens.profiles.userProfile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.ProfilePictureDialog
import com.example.vociapp.ui.components.ProfileTextField
import com.example.vociapp.ui.components.SnackbarManager
import com.example.vociapp.ui.components.getTextFieldColors
import com.example.vociapp.ui.viewmodels.AuthResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun UpdateUserProfileScreen(
    navController: NavHostController,
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.obtainAuthViewModel()
    val currentProfile = authViewModel.getCurrentUser()
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()

    val loggedUser = volunteerViewModel.getCurrentUser()
    if (loggedUser != null) {
        volunteerViewModel.getVolunteerById(loggedUser.id)
    }

    val volunteerResource by volunteerViewModel.specificVolunteer.collectAsState()

    if (currentProfile != null) {
        var photoUrl by remember { mutableStateOf("") }
        var nickname by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var surname by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }

        var isUpdating by remember { mutableStateOf(false) }
        var isInitialized by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }
        var step by remember { mutableIntStateOf(1) }
        var logging by remember { mutableStateOf(true) }

        var isPasswordCorrect by remember { mutableStateOf(true) }
        var isPhoneNumberValid by remember { mutableStateOf(true) }
        var isEmailValid by remember { mutableStateOf(true) }

        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            snackbarHost = { SnackbarManager.CustomSnackbarHost(isBottomBarVisible = true) },
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            "Modifica il profilo",
                            style = MaterialTheme.typography.headlineMedium
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
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                when (volunteerResource) {
                                    is Resource.Loading -> CircularProgressIndicator()

                                    is Resource.Success -> {
                                        val volunteer = volunteerResource.data

                                        if (!isInitialized) {
                                            nickname = volunteer?.nickname ?: ""
                                            name = volunteer?.name ?: ""
                                            surname = volunteer?.surname ?: ""
                                            email = volunteer?.email ?: ""
                                            phoneNumber = volunteer?.phone_number ?: ""
                                            photoUrl = volunteer?.photoUrl ?: ""
                                            isInitialized = true
                                        }

                                        when (step) {
                                            1 -> {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Box(
                                                        modifier = Modifier.padding(end = 16.dp)
                                                    ) {
                                                        val initials =
                                                            "${volunteer?.name?.firstOrNull() ?: ""}${volunteer?.surname?.firstOrNull() ?: ""}".uppercase()
                                                        //Profile picture
                                                        if (currentProfile.photoUrl != null) {
                                                            AsyncImage(
                                                                model = currentProfile.photoUrl,
                                                                contentDescription = "Profile Picture",
                                                                modifier = Modifier
                                                                    .size(120.dp)
                                                                    .clip(CircleShape),
                                                                contentScale = ContentScale.Crop
                                                            )
                                                        } else {

                                                            Box(
                                                                contentAlignment = Alignment.Center,
                                                                modifier = Modifier
                                                                    .size(130.dp)
                                                                    .clip(CircleShape)
                                                                    .background(MaterialTheme.colorScheme.primary)
                                                            ) {
                                                                Text(
                                                                    text = initials,
                                                                    style = MaterialTheme.typography.headlineMedium,
                                                                    fontWeight = FontWeight.Medium,
                                                                    fontSize = 60.sp
                                                                )
                                                            }
                                                        }

                                                        IconButton(
                                                            onClick = { showDialog = true },
                                                            modifier = Modifier
                                                                .align(Alignment.BottomEnd)
                                                                .size(32.dp)
                                                                .clip(CircleShape)
                                                                .background(MaterialTheme.colorScheme.secondary)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Edit,
                                                                contentDescription = "Modifica Profilo",
                                                                modifier = Modifier.size(24.dp)
                                                            )
                                                        }

                                                        ProfilePictureDialog(
                                                            showDialog = showDialog,
                                                            onDismiss = { showDialog = false },
                                                            onSave = { newPhotoUrl ->
                                                                photoUrl = newPhotoUrl
                                                                showDialog = false
                                                            },
                                                            initialPhotoUrl = photoUrl
                                                        )

                                                    }

                                                    Column(
                                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        ProfileTextField(
                                                            value = name,
                                                            onValueChange = { name = it },
                                                            label = "Nome",
                                                            placeholder = "Nome",
                                                            isLoggingIn = logging
                                                        )
                                                        ProfileTextField(
                                                            value = surname,
                                                            onValueChange = { surname = it },
                                                            label = "Cognome",
                                                            placeholder = "Cognome",
                                                            isLoggingIn = logging
                                                        )
                                                    }
                                                }

                                                ProfileTextField(
                                                    value = nickname,
                                                    onValueChange = { nickname = it },
                                                    label = "Nickname",
                                                    placeholder = "Nickname"
                                                )

                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    modifier = Modifier.align(Alignment.End)
                                                ) {
                                                    OutlinedButton(
                                                        onClick = { navController.popBackStack() },
                                                        colors = ButtonDefaults.outlinedButtonColors(
                                                            containerColor = Color.Transparent,
                                                            contentColor = MaterialTheme.colorScheme.onBackground,
                                                        ),
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.onBackground
                                                        )
                                                    ) {
                                                        Text("Annulla")
                                                    }

                                                    Button(
                                                        onClick = { isUpdating = true },
                                                        enabled = true,
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = MaterialTheme.colorScheme.primary,
                                                            contentColor = MaterialTheme.colorScheme.onPrimary,
                                                        ),
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.primary
                                                        ),

                                                        ) {
                                                        Text(
                                                            "Avanti"
                                                        )
                                                    }
                                                }
                                            }

                                            2 -> {

                                                ProfileTextField(
                                                    value = email,
                                                    onValueChange = { email = it },
                                                    label = if (isEmailValid) "Email" else "Email non valida",
                                                    placeholder = "Email",
                                                    isLoggingIn = logging,
                                                    colors = getTextFieldColors(isValid = isEmailValid)
                                                )

                                                ProfileTextField(
                                                    value = phoneNumber,
                                                    onValueChange = { phoneNumber = it },
                                                    label = if (isPhoneNumberValid) "Numero di telefono" else "Numero di telefono non valido",
                                                    placeholder = "Numero di telefono",
                                                    isLoggingIn = logging,
                                                    colors = getTextFieldColors(isValid = isPhoneNumberValid)
                                                )

                                                HorizontalDivider(
                                                    modifier = Modifier
                                                        .padding(top = 8.dp, bottom = 0.dp),
                                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.12f
                                                    ),
                                                    thickness = 1.dp
                                                )

                                                ProfileTextField(
                                                    value = password,
                                                    onValueChange = { password = it },
                                                    label = if (isPasswordCorrect) "Password" else "Password errata",
                                                    placeholder = "Password",
                                                    colors = getTextFieldColors(isValid = isPasswordCorrect),
                                                    isPassword = true,
                                                    isLoggingIn = logging
                                                )

                                                Text(
                                                    "Inserisci la password per confermare le modifiche",
                                                    modifier = Modifier
                                                        .align(Alignment.CenterHorizontally)
                                                        .offset(y = (-4).dp),
                                                    style = MaterialTheme.typography.bodySmall
                                                )

                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    modifier = Modifier.align(Alignment.End)
                                                ) {
                                                    OutlinedButton(
                                                        onClick = { step-- },
                                                        colors = ButtonDefaults.outlinedButtonColors(
                                                            containerColor = Color.Transparent,
                                                            contentColor = MaterialTheme.colorScheme.onBackground,
                                                        ),
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.onBackground
                                                        )
                                                    ) {
                                                        Text("Indietro")
                                                    }

                                                    Button(
                                                        onClick = {
                                                            isUpdating = true
                                                        },
                                                        enabled = true,
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = MaterialTheme.colorScheme.primary,
                                                            contentColor = MaterialTheme.colorScheme.onPrimary,
                                                        ),
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.primary
                                                        ),

                                                        ) {
                                                        Text(
                                                            "Conferma"
                                                        )
                                                    }
                                                }
                                            }

                                            3 -> {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                ) {
                                                    Text(
                                                        "Ti abbiamo inviato un link di verifica al seguente indirizzo email: $email",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        textAlign = TextAlign.Center,
                                                    )

                                                    Icon(
                                                        imageVector = Icons.Default.Email,
                                                        contentDescription = "Email",
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier
                                                            .size(150.dp)
                                                            .align(Alignment.CenterHorizontally)
                                                    )

                                                    Text(
                                                        "Una volta confermata la mail, verrai disconnesso e dovrai effettuare nuovamente il login con la nuova email",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        textAlign = TextAlign.Center,
                                                    )

                                                    Button(
                                                        onClick = {
                                                            coroutineScope.launch {
                                                                try {
                                                                    currentProfile.reload().await()
                                                                    SnackbarManager.showSnackbar("Non hai confermato la mail")
                                                                } catch (e: Exception) {
                                                                    if (volunteer != null) {
                                                                        volunteer.apply {
                                                                            this.name = name
                                                                            this.surname = surname
                                                                            this.nickname = nickname
                                                                            this.photoUrl = photoUrl
                                                                            this.phone_number = phoneNumber
                                                                            this.email = email
                                                                        }
                                                                        volunteerViewModel.updateVolunteer(volunteer)
                                                                    authViewModel.signInWithEmailAndPassword(email, password)
                                                                    }
                                                                }
                                                            }
                                                        },
                                                        enabled = true,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        shape = RoundedCornerShape(8.dp)
                                                    ) {
                                                        Text(
                                                            "Ho confermato la mail",
                                                            modifier = Modifier.padding(vertical = 8.dp)
                                                        )
                                                    }

                                                }
                                            }
                                        }

                                        LaunchedEffect(isUpdating) {
                                            if (isUpdating) {
                                                when (step) {
                                                    1 -> {
                                                        if (authViewModel.areFieldsEmpty(
                                                                name,
                                                                surname
                                                            )
                                                        ) {
                                                            SnackbarManager.showSnackbar("Uno o più campi sono vuoti")
                                                            isUpdating = false
                                                            return@LaunchedEffect
                                                        }
                                                        logging = true
                                                        step++
                                                        isUpdating = false
                                                    }

                                                    2 -> {
                                                        if (authViewModel.areFieldsEmpty(email, phoneNumber, password)) {
                                                            logging = false
                                                            SnackbarManager.showSnackbar("Uno o più campi sono vuoti")
                                                            isUpdating = false
                                                            return@LaunchedEffect
                                                        }

                                                        if (!authViewModel.isPhoneNumberValid(phoneNumber)) {
                                                            isPhoneNumberValid = false
                                                            SnackbarManager.showSnackbar("Numero di telefono non valido")
                                                            isUpdating = false
                                                            return@LaunchedEffect
                                                        }

                                                        //Se cambia la mail, la deve confermare
                                                        if (currentProfile.email != email) {
                                                            if (!authViewModel.isEmailValid(email)){
                                                                isEmailValid = false
                                                                SnackbarManager.showSnackbar("Email non valida")
                                                                isUpdating = false
                                                            } else {
                                                                authViewModel.reauthenticateAndVerifyEmail(email, password)
                                                                isUpdating = false
                                                                step = 3
                                                            }
                                                        } else {
                                                            val newPhotoUrl: String? =
                                                                photoUrl.ifEmpty { null }
                                                            authViewModel.updateUserProfile(nickname, newPhotoUrl)

                                                            val result = authViewModel.signInWithEmailAndPassword(volunteer!!.email, password)

                                                            if (result is AuthResult.Failure) {
                                                                isPasswordCorrect = false
                                                                SnackbarManager.showSnackbar(result.message)
                                                                isUpdating = false
                                                            } else {
                                                                volunteer.name = name
                                                                volunteer.surname = surname
                                                                volunteer.nickname = nickname
                                                                volunteer.photoUrl = photoUrl
                                                                volunteer.phone_number = phoneNumber
                                                                volunteer.email = email
                                                                volunteerViewModel.updateVolunteer(volunteer)
                                                                navController.popBackStack()
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            isPhoneNumberValid = true
                                            isPasswordCorrect = true
                                            logging = true
                                            isEmailValid = true
                                        }
                                    }

                                    is Resource.Error -> {
                                        Text(
                                            text = "Errore: ${volunteerResource.message}",
                                            color = MaterialTheme.colorScheme.error
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
}




