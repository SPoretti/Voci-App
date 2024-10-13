# Update User Profile Screen (UpdateUserProfileScreen.kt)

## Overview

This file defines the update user profile screen component for the application. The screen allows users to update their profile information such as their display name and profile picture URL. It utilizes Jetpack Compose to create a responsive and interactive UI. The update process is managed by `AuthViewModel`.

## Code Explanation

### Breakdown
- **`@Composable fun UpdateUserProfileScreen(navController: NavHostController, authViewModel: AuthViewModel)`**
    - Defines the update user profile screen as a composable function.
    - Parameters:
        - `navController`: The navigation controller to manage app navigation.
        - `authViewModel`: The view model that handles authentication and profile updates.

- **State Variables**
    - **`val currentProfile = authViewModel.getCurrentUserProfile()`**: Retrieves the current user's profile.
    - **`var displayName by remember { mutableStateOf(currentProfile?.displayName ?: "") }`**: Holds the display name input value.
    - **`var photoUrl by remember { mutableStateOf(currentProfile?.photoUrl ?: "") }`**: Holds the photo URL input value.
    - **`var showError by remember { mutableStateOf(false) }`**: Flag to show or hide error messages.
    - **`var errorMessage by remember { mutableStateOf("") }`**: Stores the error message text.
    - **`var isUpdating by remember { mutableStateOf(false) }`**: Flag to indicate if the update process is ongoing.

- **`Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))`**
    - Root container that fills the available space and sets the background color.

- **`IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(16.dp).align(Alignment.TopStart))`**
    - Back button to navigate back to the previous screen.
    - Displays an arrow icon.

- **`Column(modifier = Modifier.fillMaxSize().padding(top = 56.dp, start = 16.dp, end = 16.dp, bottom = 16.dp), ...)`**
    - Vertical layout container that holds the update profile elements.
    - Centers its children horizontally and arranges them with spaces.

- **`Text("Update Profile", style = MaterialTheme.typography.headlineMedium, ...)`**
    - Title text for the update profile screen.
    - Uses a medium headline style and bold font weight.

- **`Card(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), ...)`**
    - Displays the input fields for display name and photo URL.
    - Parameters:
        - `modifier`: Sets the width and vertical padding of the card.
        - `elevation`: Sets the card elevation for shadow effect, using `CardDefaults.cardElevation`.
        - `shape`: Sets the shape of the card with rounded corners.

- **`Column(modifier = Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp))`**
    - Inner vertical layout within the card for input fields and buttons.
    - Arranges its children with spaced margins.

- **`ProfileTextField(value = displayName, onValueChange = { displayName = it }, label = "Username", icon)`**
    - Input field for the display name, utilizing a custom `ProfileTextField` composable.
    - Displays a person icon.

- **`ProfileTextField(value = photoUrl, onValueChange = { photoUrl = it }, label = "Photo URL", icon)`**
    - Input field for the photo URL, utilizing a custom `ProfileTextField` composable.
    - Displays a face icon.

- **`Button(onClick = { isUpdating = true }, enabled = !isUpdating)`**
    - Update button.
    - Starts the update process when clicked.
    - Disabled while `isUpdating` is true to prevent multiple submissions.

## Related Files

- **[AuthViewModel.kt](AuthViewModel.md):** Manages user authentication and handles profile updates.
- **[NavGraph.kt](NavGraph.md):** Defines the navigation routes, including the update profile screen.
- **[UserProfileScreen.kt](UserProfileScreen.md):** Companion screen for viewing user profiles within the user management flow.

## Usage

The `UpdateUserProfileScreen` is used for updating user profile information within the app. Here is how it can be integrated and used:

### Example Usage in NavGraph

Include the `UpdateUserProfileScreen` in the navigation graph to enable navigation to the update profile screen:

```kotlin
composable(route = Screens.UpdateProfile.route) {
    UpdateUserProfileScreen(navController = navController, authViewModel = authViewModel)
}
```

### Handling Profile Update

The update button initiates the profile update process, managed by `AuthViewModel`:

```kotlin
Button(
    onClick = {
        isUpdating = true
        // Update user profile
        viewModelScope.launch {
            val result = authViewModel.updateUserProfile(displayName, photoUrl)
            isUpdating = false
            if (result is AuthResult.Success) {
                // Navigate back to profile screen
                navController.navigate(Screens.UserProfile.route)
            } else if (result is AuthResult.Failure) {
                // Display error message
                showError = true
                errorMessage = result.message
            }
        }
    },
    enabled = !isUpdating
) {
    Text("Update Profile")
}
```

## Additional Notes

- The screen provides a clean and user-friendly interface for updating user profiles.
- It ensures responsive design and optimal user experience across different devices.
- Proper state management and error handling enhance the reliability of the update process.