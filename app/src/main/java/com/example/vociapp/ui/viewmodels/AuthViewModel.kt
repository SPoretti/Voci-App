package com.example.vociapp.ui.viewmodels

import android.content.ContentValues.TAG
import android.net.Uri
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
            handleAuthException(e)
        } catch (e: Exception) {
            handleGenericException(e)
        }
    }

    //TODO: spostare in una classe a parte
    private fun handleAuthException(e: FirebaseAuthException): AuthResult {
        Log.e("Auth", "Errore FirebaseAuthException: ${e.message}", e)
        return if (e.message?.contains("we have blocked all requests", ignoreCase = true) == true) {
            AuthResult.Failure("Limite di tentativi raggiunto, riprova più tardi")
        } else {
            AuthResult.Failure("Errore di autenticazione: ${e.message}")
        }
    }

    private fun handleGenericException(e: Exception): AuthResult {
        Log.e("Auth", "Errore generico: ${e.message}", e)
        return if (e.message?.contains("we have blocked all requests", ignoreCase = true) == true) {
            AuthResult.Failure("Limite di tentativi raggiunto, riprova più tardi")
        } else {
            AuthResult.Failure("Errore sconosciuto: ${e.message}")
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Failure(e.message ?: "An unknown error occurred")
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun updateUserProfile(displayName: String?, photoUrl: String?): AuthResult {
        return try {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(photoUrl?.let { Uri.parse(it) })
                .build()

            auth.currentUser?.updateProfile(profileUpdates)?.await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Failure(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun updateUserProfile(displayName: String?): AuthResult {
        return try {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            Log.d(TAG, "updateUserProfile: ${profileUpdates.displayName}")

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
}

sealed class AuthResult {
    object Success : AuthResult()
    data class Failure(val message: String) : AuthResult()
}

data class UserProfile(
    val displayName: String? = null,
    val photoUrl: String? = null
)
