# Authentication ViewModel (AuthViewModel.kt)

## Overview

This file defines the `AuthViewModel` class, which handles user authentication and profile management. It interacts with Firebase Authentication to perform actions such as sign-in, sign-out, user creation, and profile updates. The class utilizes Kotlin Coroutines and StateFlow to manage and observe the authentication state.

## Code Explanation

### Breakdown
- **`class AuthViewModel : ViewModel()`**
    - Inherits from `ViewModel` to manage UI-related data in a lifecycle-conscious way.

- **`private val _authState = MutableStateFlow<AuthState>(AuthState.Uninitialized)`**
    - Holds the mutable state of the authentication status.
    - Initially set to `Uninitialized`.

- **`val authState: StateFlow<AuthState> = _authState.asStateFlow()`**
    - Exposes a read-only version of `_authState`.

- **`private val auth: FirebaseAuth = FirebaseAuth.getInstance()`**
    - Initializes an instance of Firebase Authentication.

- **`private val authStateListener = FirebaseAuth.AuthStateListener { ... }`**
    - Listener that updates the `_authState` based on the current user authentication state.
    - Sets the state to `Authenticated` if a `FirebaseUser` is present; otherwise, sets it to `Unauthenticated`.

- **`init { ... }`**
    - Adds the `authStateListener` to Firebase Authentication when the `AuthViewModel` is initialized.

- **`override fun onCleared() { ... }`**
    - Removes the `authStateListener` when the `ViewModel` is cleared to prevent memory leaks.

- **`suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult`**
    - Signs in a user with an email and password.
    - Returns `AuthResult.Success` if successful or `AuthResult.Failure` with an error message if unsuccessful.

- **`suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult`**
    - Creates a new user account with an email and password.
    - Returns `AuthResult.Success` if successful or `AuthResult.Failure` with an error message if unsuccessful.

- **`fun signOut()`**
    - Signs out the current user.

- **`suspend fun updateUserProfile(displayName: String?, photoUrl: String?): AuthResult`**
    - Updates the user's profile information such as display name and profile photo.
    - Returns `AuthResult.Success` if successful or `AuthResult.Failure` with an error message if unsuccessful.

- **`fun getCurrentUserProfile(): UserProfile?`**
    - Retrieves the current user's profile information.
    - Returns a `UserProfile` object or null if no user is logged in.

- **`fun getCurrentUser(): FirebaseUser?`**
    - Retrieves the currently authenticated `FirebaseUser`.

### Helper Classes and Sealed Classes
- **`sealed class AuthResult`**
    - Represents the result of an authentication operation.
    - Contains two subclasses: `Success` and `Failure`.

- **`data class UserProfile(val displayName: String? = null, val photoUrl: String? = null)`**
    - Data class that stores user profile information such as display name and photo URL.

## Related Files

- **[UserProfileScreen.kt](UserProfileScreen.md):** Utilizes `AuthViewModel` to display and manage user profile information.
- **[NavGraph.kt](NavGraph.md):** Defines the navigation routes including those for authentication-related screens (sign-in, sign-up).
- **[MainActivity.kt](MainActivity.md):** Sets up the main structure and integrates the `AuthViewModel`.

## Usage

The `AuthViewModel` is used to manage authentication and user profile operations within the app. Here's how it can be integrated and used:

### Example Usage in Composable

Integrate the `AuthViewModel` in your composable screen to access and manipulate authentication data:

```kotlin
@Composable
fun UserProfileScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val userProfile = authViewModel.getCurrentUserProfile()
    // UI elements to display user profile data
}
```

### Handling Authentication

The `AuthViewModel` provides methods for sign-in, sign-out, user creation, and profile updates:

```kotlin
// Sign in with email and password
viewModelScope.launch {
    val result = authViewModel.signInWithEmailAndPassword(email, password)
    if (result is AuthResult.Success) {
        // Handle successful sign-in
    } else if (result is AuthResult.Failure) {
        // Handle sign-in failure
    }
}

// Sign out the current user
authViewModel.signOut()

// Update user profile
viewModelScope.launch {
    val result = authViewModel.updateUserProfile(displayName, photoUrl)
    if (result is AuthResult.Success) {
        // Handle successful profile update
    } else if (result is AuthResult.Failure) {
        // Handle profile update failure
    }
}
```

## Additional Notes

- Using `StateFlow` allows observing authentication state changes efficiently in a Compose UI.
- Incorporates best practices for handling authentication operations with Firebase in a Kotlin-based Android app.
- Proper usage and disposal of `AuthStateListener` prevent memory leaks and ensure the application remains responsive.