# Sign-Up Screen (SignUpScreen.kt)

## Overview

This file defines the sign-up screen component for the application. The screen allows users to create a new account using their email and password. It utilizes Jetpack Compose to create a responsive and interactive UI. The authentication process is managed by `AuthViewModel`.

## Code Explanation

### Breakdown
- **`@Composable fun SignUpScreen(navController: NavHostController, authViewModel: AuthViewModel)`**
    - Defines the sign-up screen as a composable function.
    - Parameters:
        - `navController`: The navigation controller to manage app navigation.
        - `authViewModel`: The view model that handles authentication.

- **State Variables**
    - **`var email by remember { mutableStateOf("") }`**: Holds the email input value.
    - **`var password by remember { mutableStateOf("") }`**: Holds the password input value.
    - **`var confirmPassword by remember { mutableStateOf("") }`**: Holds the confirmation password input value.
    - **`var showError by remember { mutableStateOf(false) }`**: Flag to show or hide error messages.
    - **`var errorMessage by remember { mutableStateOf("") }`**: Stores the error message text.
    - **`var isSigningUp by remember { mutableStateOf(false) }`**: Flag to indicate if sign-up process is ongoing.

- **`Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))`**
    - Root container that fills the available space and sets the background color.

- **`Column(modifier = Modifier.fillMaxSize().padding(24.dp), ...)`**
    - Vertical layout container that holds the sign-up elements.
    - Centers its children both horizontally and vertically.

- **`Text("Create Account", style = MaterialTheme.typography.headlineLarge, ...)`**
    - Title text for the sign-up screen.
    - Uses the primary color and bold font weight.

- **`Spacer(modifier = Modifier.height(32.dp))`**
    - Adds vertical space between UI elements.

- **`Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), ...)`**
    - Displays the input fields for email, password, and confirmation password.
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

- **`AuthTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirm Password", icon = Icons.Default.Lock, isPassword = true)`**
    - Input field for confirming the password.
    - Ensures the entered password matches the confirmation password.

- **`Button(onClick = { isSigningUp = true }, enabled = !isSigningUp)`**
    - Sign-up button.
    - Starts the sign-up process when clicked.
    - Disabled while `isSigningUp` is true to prevent multiple submissions.

## Related Files

- **[AuthViewModel.kt](AuthViewModel.md):** Manages user authentication and handles sign-up operations.
- **[NavGraph.kt](NavGraph.md):** Defines the navigation routes, including the sign-up screen.
- **[SignInScreen.kt](SignInScreen.md):** Companion screen for user sign-in within the authentication flow.

## Usage

The `SignUpScreen` is used for user registration within the app. Here is how it can be integrated and used:

### Example Usage in NavGraph

Include the `SignUpScreen` in the navigation graph to enable navigation to the sign-up screen:

```kotlin
composable(route = Screens.SignUp.route) {
    SignUpScreen(navController = navController, authViewModel = authViewModel)
}
```

### Handling Sign-Up

The sign-up button initiates the registration process, managed by `AuthViewModel`:

```kotlin
Button(
    onClick = {
        isSigningUp = true
        // Register user
        viewModelScope.launch {
            if (password != confirmPassword) {
                showError = true
                errorMessage = "Passwords do not match"
                isSigningUp = false
                return@launch
            }

            val result = authViewModel.createUserWithEmailAndPassword(email, password)
            isSigningUp = false
            if (result is AuthResult.Success) {
                // Navigate to sign-in or home screen
                navController.navigate(Screens.SignIn.route)
            } else if (result is AuthResult.Failure) {
                // Display error message
                showError = true
                errorMessage = result.message
            }
        }
    },
    enabled = !isSigningUp
) {
    Text("Create Account")
}
```

## Additional Notes

- The screen provides a clean and user-friendly interface for user registration.
- It ensures responsive design and optimal user experience across different devices.
- Proper state management and error handling enhance the reliability of the registration process.
- Ensures password and confirmation password match to prevent user errors.