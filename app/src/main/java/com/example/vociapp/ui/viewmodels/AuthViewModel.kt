package com.example.vociapp.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.vociapp.data.util.AuthState
import com.google.firebase.auth.FirebaseAuth
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
            val result = auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Failure(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
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
    data object Success : AuthResult()//da reversare forse
    data class Failure(val message: String) : AuthResult()
}

data class UserProfile(
    val displayName: String? = null,
    val photoUrl: String? = null
)
