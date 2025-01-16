package com.voci.app.data.util

import com.google.firebase.auth.FirebaseUser

// Sealed class for authentication states
sealed class AuthState {
    data object Uninitialized : AuthState()
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
}