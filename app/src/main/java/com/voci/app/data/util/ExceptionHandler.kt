package com.voci.app.data.util

import com.voci.app.ui.viewmodels.AuthResult
import com.google.firebase.auth.FirebaseAuthException

class ExceptionHandler {
    fun handleAuthException(e: FirebaseAuthException): AuthResult {
        return if (e.message?.contains("we have blocked all requests", ignoreCase = true) == true) {
            AuthResult.Failure("Limite di tentativi raggiunto, riprova più tardi")
        } else {
            AuthResult.Failure("Errore di autenticazione: ${e.message}")
        }
    }

    fun handleGenericException(e: Exception): AuthResult {
        return if (e.message?.contains("we have blocked all requests", ignoreCase = true) == true) {
            AuthResult.Failure("Limite di tentativi raggiunto, riprova più tardi")
        } else {
            AuthResult.Failure("Errore sconosciuto: ${e.message}")
        }
    }
}