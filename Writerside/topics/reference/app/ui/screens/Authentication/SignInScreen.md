# Sign-In Screen (SignInScreen.kt)

## Overview

This file defines the sign-in screen component for the application. The screen allows users to sign in using their email and password. It utilizes Jetpack Compose to create a responsive and interactive UI. The authentication process is managed by `AuthViewModel`.

## Code Explanation

### Breakdown
- **`@Composable fun SignInScreen(navController: NavHostController, authViewModel: AuthViewModel)`**
    - Defines the sign-in screen as a composable function.
    - Parameters:
        - `navController`: The navigation controller to manage app navigation.
        - `authViewModel`: The view model that handles authentication.

- **State Variables**
    - **`var email by remember { mutableStateOf("") }`**: Holds the email input value.
    - **`var password by remember { mutableStateOf("") }`**: Holds the password input value.
    - **`var showError by remember { mutableStateOf(false) }`**: Flag to show or hide error messages.
    - **`var errorMessage by remember { mutableStateOf("") }`**: Stores the error message text.
    - **`var isSigningIn by remember { mutableStateOf(false) }`**: Flag to indicate if sign-in process is ongoing.

- **`Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))`**
    - Root container that fills the available space and sets the background color.

- **`Column(modifier = Modifier.fillMaxSize().padding(24.dp), ...)`**
    - Vertical layout container that holds the sign-in elements.
    - Centers its children both horizontally and vertically.

- **`Text("Sign In", style = MaterialTheme.typography.headlineLarge, ...)`**
    - Title text for the sign-in screen.
    - Uses the primary color and bold font weight.

- **`Spacer(modifier = Modifier.height(32.dp))`**
    - Adds vertical space between UI elements.

- **`Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), ...)`**
    - Displays the input fields for email and password.
    - Parameters:
        - `modifier`: Sets the width and horizontal padding of the card.
        - `elevation`: Sets the card elevation for shadow effect, using `CardDefaults.cardElevation`.
        - `shape`: Sets the shape of the card with rounded corners.

- **`Column(modifier = Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp))`**
    - Inner vertical layout within the card for input fields and buttons.
    - Arranges its children with spaced margins.

- **`AuthTextField(value = email, onValueChange = { email = it }, label = "Email", icon = Icons.Default.Email)`**
    - Input field for email, utilizing the custom `AuthTextField` composable.
    - Displays an email icon.

- **`AuthTextField(value = password, onValueChange = { password = it }, label = "Password", icon = Icons.Default.Lock, isPassword = true)`**
    - Input field for password, utilizing the custom `AuthTextField` composable.
    - Sets `isPassword` to true for input masking.

- **`Button(onClick = { isSigningIn = true }, enabled = !isSigningIn)`**
    - Sign-in button.
    - Starts the sign-in process when clicked.
    - Disabled while `isSigningIn` is true to prevent multiple submissions.

## Related Files

- **[AuthViewModel.kt](AuthViewModel.md):** Manages user authentication and handles sign-in operations.
- **[NavGraph.kt](NavGraph.md):** Defines the navigation routes, including the sign-in screen.
- **[UserProfileScreen.kt](UserProfileScreen.md):** Navigates to the user profile screen upon successful sign-in.

## Usage

The `SignInScreen` is used for user authentication within the app. Here is how it can be integrated and used:

### Example Usage in NavGraph

Include the `SignInScreen` in the navigation graph to enable navigation to the sign-in screen:

```kotlin
composable(route = Screens.SignIn.route) {
    SignInScreen(navController = navController, authViewModel = authViewModel)
}
```

### Handling Sign-In

The sign-in button initiates the authentication process, managed by `AuthViewModel`:

```kotlin
Button(
    onClick = {
        isSigningIn = true
        // Authenticate user
        viewModelScope.launch {
            val result = authViewModel.signInWithEmailAndPassword(email, password)
            isSigningIn = false
            if (result is AuthResult.Success) {
                // Navigate to user profile or home screen
                navController.navigate(Screens.UserProfile.route)
            } else if (result is AuthResult.Failure) {
                // Display error message
                showError = true
                errorMessage = result.message
            }
        }
    },
    enabled = !isSigningIn
) {
    Text("Sign In")
}
```

## Additional Notes

- The screen provides a clean and user-friendly interface for user authentication.
- It ensures responsive design and optimal user experience across different devices.
- Proper state management and error handling enhance the reliability of the authentication process.