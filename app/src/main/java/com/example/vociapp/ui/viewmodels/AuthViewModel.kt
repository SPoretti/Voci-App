package com.example.vociapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vociapp.data.types.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import com.example.vociapp.data.util.ExceptionHandler

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Uninitialized)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        _authState.value = if (firebaseUser != null) {
            AuthState.Authenticated(firebaseUser)
        } else {
            AuthState.Unauthenticated
        }
    }

    private val exceptionHandler = ExceptionHandler()

    init {
        auth.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Failure("Password errata")
        } catch (e: FirebaseAuthException) {
            exceptionHandler.handleAuthException(e)
        } catch (e: IllegalArgumentException) {
            AuthResult.Failure("Uno o più campi sono vuoti")
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: IllegalArgumentException) {
            AuthResult.Failure("Uno o più campi sono vuoti")
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun updateUserProfile(displayName: String?): AuthResult {
        Log.d("AuthViewModel", "updateUserProfile called with displayName: $displayName")
        return try {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            auth.currentUser?.updateProfile(profileUpdates)?.await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Failure(e.message ?: "An unknown error occurred")
        }
    }

    fun getCurrentUserProfile(): UserProfile? {
        val user = auth.currentUser ?: return null
        return UserProfile(
            displayName = user.displayName,
            photoUrl = user.photoUrl?.toString()
        )
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
    }
}

sealed class AuthResult {
    data object Success : AuthResult()
    data class Failure(val message: String) : AuthResult()
}

data class UserProfile(
    val displayName: String? = null,
    val photoUrl: String? = null
)
