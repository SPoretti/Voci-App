package com.example.vociapp.ui.screens.profiles.userProfile

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.EditText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.R
//import androidx.preference.isNotEmpty
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.viewmodels.AuthResult
import com.example.vociapp.ui.viewmodels.VolunteerViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.vociapp.data.repository.VolunteerRepository
import com.example.vociapp.data.types.Volunteer
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.util.Resource
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.tasks.await


@Composable
fun UpdateUserProfileScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.getAuthViewModel()
    val currentProfile = authViewModel.getCurrentUserProfile()
    var displayName by remember { mutableStateOf(currentProfile?.displayName ?: "") }
    // val surname by remember { mutableStateOf(currentProfile?.surname ?: "") }
    var email by remember { mutableStateOf(currentProfile?.email ?: "") }
    var phoneNumber by remember { mutableStateOf(currentProfile?.phoneNumber ?: "") }
    var photoUrl by remember { mutableStateOf(currentProfile?.photoUrl ?: "") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isUpdating by remember { mutableStateOf(false) }
    var isNavigatingBack by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") } // Add a state for password input

    var currentPassword by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }


    /*LaunchedEffect(key1 = "updateUserProfile") {
        isNavigatingBack = false
        isUpdating = false
    }*/

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        IconButton(
            onClick = {
                isNavigatingBack = true
                navController.popBackStack()
            },
            enabled = !isNavigatingBack,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to Profile",
                tint = MaterialTheme.colorScheme.primary
            )
        }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Update Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileTextField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        label = "Name",
                        icon = Icons.Default.Person
                    )

//                    ProfileTextField(
//                        value = surname,
//                        onValueChange = { surname = it },
//                        label = "Name",
//                        icon = Icons.Default.Person
//                    )

                    ProfileTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        icon = Icons.Default.Email
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        )
                    )

                    ProfileTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = "Phone Number",
                        icon = Icons.Default.Phone
                    )

                    ProfileTextField(
                        value = photoUrl,
                        onValueChange = { photoUrl = it },
                        label = "Profile Picture URL",
                        icon = Icons.Default.Face
                    )

                    OutlinedTextField(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        label = { Text("New Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        )
                    )

                    // Add a field for password input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        )
                    )

                    Button(
                        onClick = {
                            isUpdating = true
                        },
                        enabled = !isUpdating,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            if (isUpdating) "Updating..." else "Update Profile",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
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
        }
    }

    LaunchedEffect(isUpdating) {
        if (isUpdating) {
            // funziona trova "id" del profilo utente
            val currentId = serviceLocator.getVolunteerViewModel().getVolunteerId().toString()
//            println("this is the document id: " + currentId)
            // Usage
            val db = FirebaseFirestore.getInstance()
            // TODO: modifica-> in modo che trova direttamente l utente
            val docRef = db.collection("volunteers").document("bptEAkUWtN879UNFni84")

            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser

            val updates = mapOf(
                "name" to displayName,
                // "email" to email,
                "phone_number" to phoneNumber,
                "photoUrl" to photoUrl
            ).filterValues { it.isNotEmpty() }

            // attiva la funzione updateVolunteer che dovrebbe modificare solo i campi inseriti ma modifica anche quelli già presenti nel database (-> null)
//            val result = serviceLocator.getVolunteerViewModel().updateVolunteer(updates)

            if (updates.isNotEmpty()) {
                try {
                    val querySnapshot = db.collection("volunteers")
                        .whereEqualTo("id", currentId) // Query by "id" field
                        .get()
                        .await()

                    if (querySnapshot.documents.isNotEmpty()) {
                        val documentId = querySnapshot.documents[0].id // Get the document ID
                        db.collection("volunteers")
                            .document(documentId) // Use the document ID for update
                            .set(updates, SetOptions.merge())
                            .await()
                        Resource.Success(Unit)
                    } else {
                        Resource.Error("Volunteer not found") // Handle case where request is not found
                    }
                } catch (e: Exception) {
                    Resource.Error(e.message ?: "An unknown error occurred")
                }
            }

            currentUser?.let {
                it.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(
                            "UpdateEmail",
                            "User email address updated."
                        )
                        Toast.makeText (context, "Verification email sent to update address.", Toast.LENGTH_SHORT).show()
                    } else {
                        task.exception?.let { exception ->
                            Log.e(
                                "UpdateEmail",
                                "Failed to send verification email: ${exception.message}"
                            )
                            Toast.makeText (context, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } ?: run {
                Toast.makeText(
                    context,
                    "No user is currently signed in.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val user = Firebase.auth.currentUser

            user!!.verifyBeforeUpdateEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User email address updated.")
                    }
                }

        }
        isUpdating = false
    }
}



@Composable
fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    )
}

@Composable
fun CollectProfileUpdates(
    displayName: String,
    email: String,
    phoneNumber: String,
    photoUrl: String
): Map<String, Any> {
    return mapOf(
        "displayName" to displayName,
        "email" to email,
        "phoneNumber" to phoneNumber,
        "photoUrl" to photoUrl
    ).filterValues { it.isNotEmpty() }
}
