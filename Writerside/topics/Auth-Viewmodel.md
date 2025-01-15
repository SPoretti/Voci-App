# Authentication [Viewmodel]

The `AuthViewModel` class handles user authentication logic in the application. It manages the authentication state, facilitates signing in and signing up, and provides utility functions for user authentication, profile management, and email verification.

## State Variables

### Auth State
- **Variable:** `_authState`
- **Public Access:** `authState`
- **Type:** `StateFlow<AuthState>`
- **Description:** Holds the current authentication state. It can either be `Authenticated`, `Unauthenticated`, or `Uninitialized`.

---

## Methods

### onCleared
- **Description:** Cleans up the `AuthStateListener` when the ViewModel is cleared.

### signOut
- **Description:** Signs the current user out of the application.
- **Usage:** Called when the user requests to sign out.

### signInWithEmailAndPassword
- **Description:** Signs in a user with their email and password.
- **Parameters:**
    - `email`: `String` - The email of the user.
    - `password`: `String` - The password of the user.
- **Returns:** `AuthResult` - Success or Failure based on the outcome.

### createUserWithEmailAndPassword
- **Description:** Creates a new user with their email and password.
- **Parameters:**
    - `email`: `String` - The email of the user.
    - `password`: `String` - The password of the user.
- **Returns:** `AuthResult` - Success or Failure based on the outcome.

### areFieldsEmpty
- **Description:** Verifies if any of the provided fields are empty.
- **Parameters:**
    - `fields`: `vararg String` - The fields to check.
- **Returns:** `Boolean` - True if any field is empty.

### isPhoneNumberValid
- **Description:** Verifies if the provided phone number is valid.
- **Parameters:**
    - `phoneNumber`: `String` - The phone number to check.
- **Returns:** `Boolean` - True if the phone number matches the valid pattern.

### isPasswordValid
- **Description:** Verifies if the provided password is valid.
- **Parameters:**
    - `password`: `String` - The password to check.
- **Returns:** `Boolean` - True if the password matches the valid pattern.

### isEmailValid
- **Description:** Verifies if the provided email is valid.
- **Parameters:**
    - `email`: `String` - The email to check.
- **Returns:** `Boolean` - True if the email matches the valid pattern.

### updateUserProfile
- **Description:** Updates the current user's profile with a new display name and photo URL.
- **Parameters:**
    - `displayName`: `String?` - The new display name.
    - `photoUrl`: `String?` - The new photo URL.
- **Returns:** `AuthResult` - Success or Failure based on the outcome.

### getCurrentUserProfile
- **Description:** Retrieves the current user's profile details (display name and photo URL).
- **Returns:** `UserProfile?` - The current user's profile or null if no user is authenticated.

### getCurrentUser
- **Description:** Retrieves the current authenticated user.
- **Returns:** `FirebaseUser?` - The current authenticated user or null if no user is authenticated.

### sendVerificationEmail
- **Description:** Sends a verification email to the current user.
- **Usage:** Called to send an email to verify the user's email address.

### reauthenticateAndVerifyEmail
- **Description:** Re-authenticates the user and verifies their email address before updating it.
- **Parameters:**
    - `newEmail`: `String` - The new email address.
    - `password`: `String` - The current password for reauthentication.
- **Usage:** Called when updating the user's email address.

### sendPasswordResetEmail
- **Description:** Sends a password reset email to the user.
- **Parameters:**
    - `email`: `String` - The email address to which the reset email will be sent.
- **Returns:** `AuthResult` - Success or Failure based on the outcome.

---

## Dependencies

- **FirebaseAuth:**
    - Provides authentication functionalities for the application.
- **ExceptionHandler:**
    - Handles authentication exceptions and errors.

---

## Sealed Classes

### AuthResult
- **Success**: Represents a successful authentication result.
- **Failure**: Represents a failed authentication result with an associated error message.

### UserProfile
- **displayName**: `String?` - The user's display name.
- **photoUrl**: `String?` - The user's photo URL.

---
