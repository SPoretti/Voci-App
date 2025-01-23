package com.voci.app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.voci.app.data.util.AuthState
import com.voci.app.data.util.ExceptionHandler
import com.voci.app.data.util.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val networkManager: NetworkManager
): ViewModel() {
    //---------- State variables for authentication ----------
    private val _authState = MutableStateFlow<AuthState>(AuthState.Uninitialized)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    // Firebase Authentication instance
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    // Authentication state listener
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        _authState.value = if (firebaseUser != null) {
            AuthState.Authenticated(firebaseUser)
        } else {
            AuthState.Unauthenticated
        }
    }
    // Exception handler
    private val exceptionHandler = ExceptionHandler()
    // Initialize the ViewModel
    init {
        auth.addAuthStateListener(authStateListener)
    }
    //---------- Authentication methods -----------------
    // Clear the ViewModel
    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }
    // SignOut
    fun signOut() {
        auth.signOut()
    }
    // Sign in with email and password
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        return if (networkManager.isNetworkConnected()){
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                AuthResult.Success
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                Log.e("AuthViewModel", "signInWithEmailAndPassword: ${e.message}")
                AuthResult.Failure("Credenziali non valide")
            } catch (e: FirebaseAuthException) {
                exceptionHandler.handleAuthException(e)
            } catch (e: IllegalArgumentException) {
                Log.e("AuthViewModel", "signInWithEmailAndPassword: ${e.message}")
                AuthResult.Failure("Uno o più campi sono vuoti")
            }
        } else {
            AuthResult.Failure("Nessuna connessione ad internet")
        }
    }
    // Sign up with email and password
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult {
        return if (networkManager.isNetworkConnected()){
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                AuthResult.Success
            } catch (e: FirebaseAuthUserCollisionException) {
                Log.e("AuthViewModel", "createUserWithEmailAndPassword: ${e.message}")
                AuthResult.Failure("L'email è già associata a un altro account.")
            } catch (e: Exception) {
                AuthResult.Failure("Errore nella registrazione: ${e.message}")
            }
        } else{
            AuthResult.Failure("Nessuna connessione ad internet")
        }
    }
    // Method to verify if fields are empty
    fun areFieldsEmpty(vararg fields: String): Boolean {
        return fields.any { it.trim().isEmpty() }
    }
    // Method to verify if the phone number is valid
    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val phoneNumberPattern = "^\\+?[0-9]{10,15}\$"
        return phoneNumber.matches(phoneNumberPattern.toRegex())
    }
    // Method to verify if the password is valid
    fun isPasswordValid(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!#%*?&])[A-Za-z\\d@$!#%*?&]{8,}$"
        return password.matches(passwordPattern.toRegex())
    }
    // Method to verify if the email is valid
    fun isEmailValid(email: String): Boolean {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
        return email.matches(Regex(emailPattern))
    }
    // Update user profile
    suspend fun updateUserProfile(displayName: String?, photoUrl: String?): AuthResult {
        return if (networkManager.isNetworkConnected()){
            try {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(photoUrl?.let { android.net.Uri.parse(it) })
                    .build()
                auth.currentUser?.updateProfile(profileUpdates)?.await()
                AuthResult.Success
            } catch (e: Exception) {
                AuthResult.Failure(e.message ?: "An unknown error occurred")
            }
        } else{
            AuthResult.Failure("Nessuna connessione ad internet")
        }
    }
    // Get the current user's profile
    fun getCurrentUserProfile(): UserProfile? {
        val user = auth.currentUser ?: return null
        return UserProfile(
            displayName = user.displayName,
            photoUrl = user.photoUrl?.toString()
        )
    }
    // Get the current user from Firebase
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    // Send a verification email to the user
    fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
    }
    // Reauthenticate and verify the user's email
    fun reauthenticateAndVerifyEmail(newEmail: String, password: String) {
        val user = auth.currentUser
        if (user != null) {
            val cred = EmailAuthProvider.getCredential(user.email ?: "", password)

            user.reauthenticate(cred).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener { verifyTask ->
                        if (verifyTask.isSuccessful) {
                            Log.d("AuthViewModel", "Email di verifica inviata a: $newEmail")
                        } else {
                            Log.e("AuthViewModel", "Errore nell'invio dell'email di verifica: ${verifyTask.exception?.message}")
                        }
                    }
                } else {
                    Log.e("AuthViewModel", "Reautenticazione fallita: ${task.exception?.message}")
                }
            }
        }
    }
    // Send a password reset email to the user
    fun sendPasswordResetEmail(email: String): AuthResult {
        return if (networkManager.isNetworkConnected()){
            try {
                auth.sendPasswordResetEmail(email)
                Log.d("AuthViewModel", "Password reset email sent to: $email")
                AuthResult.Success
            } catch (e: FirebaseAuthEmailException) {
                Log.e("AuthViewModel", "sendPasswordResetEmail: ${e.message}")
                AuthResult.Failure("Email non valida")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "sendPasswordResetEmail: ${e.message}")
                AuthResult.Failure("Errore nell'invio dell'email")
            }
        } else{
            AuthResult.Failure("Nessuna connessione ad internet")
        }
    }
}
// Authentication result
sealed class AuthResult {
    data object Success : AuthResult()
    data class Failure(val message: String) : AuthResult()
}
// User profile
data class UserProfile(
    val displayName: String? = null,
    val photoUrl: String? = null
)
