package com.example.vociapp.data.util

import com.google.firebase.auth.FirebaseUser

sealed class AuthState {
    object Uninitialized : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
}