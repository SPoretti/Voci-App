package com.example.vociapp.ui.screens.auth

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.vociapp.di.LocalServiceLocator

@Composable
fun EmailVerification(
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val authViewModel = serviceLocator.getAuthViewModel()
    val user = authViewModel.getCurrentUser()

    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    isLoading = true
    user!!.sendEmailVerification()
        .addOnCompleteListener { task ->
            isLoading = false
            if (task.isSuccessful) {
                message = "Email verification sent. Please check your inbox."
            } else {
                message = "Failed to send email verification: ${task.exception?.localizedMessage}"
            }
        }

    if (isLoading) {
        CircularProgressIndicator()
    } else if (message != null) {
        Text(text = message!!)
    }

}
