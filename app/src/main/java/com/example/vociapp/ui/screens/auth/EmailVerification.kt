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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.SnackbarManager

@Composable
fun EmailVerification(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.obtainAuthViewModel()
    val user = authViewModel.getCurrentUser()
    
    var emailVerified by remember { mutableStateOf(user?.isEmailVerified ?: false) }

    LaunchedEffect(user) {
        while (true) {
            user?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emailVerified = user.isEmailVerified
                    Log.d("EmailVerification", "Email verified: $emailVerified")
                }
            }
            kotlinx.coroutines.delay(3000)
        }
    }

    LaunchedEffect(emailVerified) {
            if (emailVerified) {
                Log.d("EmailVerification", "Email verified")
                SnackbarManager.showSnackbar("Email confermata!")
                navController.navigate("completeSignUp") {
                    popUpTo("emailVerification") { inclusive = true }
                }
            }
    }

    if (user != null) {
        Scaffold(
            snackbarHost = { SnackbarManager.CustomSnackbarHost() },
            content = { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(MaterialTheme.colorScheme.background)
                )
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
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(78.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(
                                "Ti abbiamo inviato un link di verifica al seguente indirizzo email: ${user.email}",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        )
    }
}