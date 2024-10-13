# User Profile Screen (UserProfileScreen.kt)

## Overview

This file defines the user profile screen component for the application. The screen displays the user's profile information and provides options for editing the profile and logging out. It utilizes Jetpack Compose to create a responsive and interactive UI.

## Code Explanation

### Breakdown
- **`@Composable fun UserProfileScreen(navController: NavHostController, authViewModel: AuthViewModel)`**
    - Defines the user profile screen as a composable function.
    - Parameters:
        - `navController`: The navigation controller to manage app navigation.
        - `authViewModel`: The view model that handles authentication and user data.

- **`val userProfile = authViewModel.getCurrentUserProfile()`**
    - Retrieves the current user's profile information from the `AuthViewModel`.

- **`Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))`**
    - Root container that fills the available space and sets the background color.

- **`Column(modifier = Modifier.fillMaxSize().padding(16.dp), ...)`**
    - Vertical layout container that holds the profile elements.
    - Centers its children both horizontally and vertically.

- **`Card(...)`**
    - Displays the user's profile information within a card.
    - Parameters:
        - `modifier`: Sets the width and padding of the card.
        - `elevation`: Sets the card elevation for shadow effect, using `CardDefaults.cardElevation`.
        - `shape`: Sets the shape of the card with rounded corners.

- **`IconButton(onClick = { ... }, modifier = Modifier.align(Alignment.TopStart))`**
    - Button for editing the profile, positioned at the top-left of the card.
    - Navigates to the `UpdateUserProfile` screen when clicked.
    - Uses `Icons.Default.Edit` for the edit icon.

- **`IconButton(onClick = { authViewModel.signOut() }, modifier = Modifier.align(Alignment.TopEnd))`**
    - Button for logging out, positioned at the top-right of the card.
    - Signs the user out when clicked.
    - Uses `Icons.AutoMirrored.Filled.ExitToApp` for the logout icon.

- **`Column(modifier = Modifier.fillMaxWidth().padding(24.dp), ...)`**
    - Inner vertical layout within the card for profile details.
    - Arranges its children with spaced margins.

## Related Files

- **[NavGraph.kt](NavGraph.md):** Defines the navigation graph and routes, including `UpdateUserProfile`.
- **[AuthViewModel.kt](AuthViewModel.md):** Manages user authentication and provides profile information.
- **[Screens.kt](Screens.md):** Defines the screen routes and properties, such as `UpdateUserProfile`.

## Usage

The `UserProfileScreen` is used to display and manage the user's profile within the app. Here is how it integrates with other components:

### Example Usage in NavGraph

The `UserProfileScreen` is included in the navigation graph to facilitate navigation:

```kotlin
composable(route = Screens.UserProfile.route) {
    UserProfileScreen(navController = navController, authViewModel = authViewModel)
}
```

### Handling User Actions

Edit and logout actions are handled via icon buttons within the `Card`:

```kotlin
// Edit button
IconButton(
    onClick = { navController.navigate(Screens.UpdateUserProfile.route) },
    modifier = Modifier.align(Alignment.TopStart)
) {
    Icon(
        imageVector = Icons.Default.Edit,
        contentDescription = "Edit Profile",
        tint = MaterialTheme.colorScheme.primary
    )
}

// Logout button
IconButton(
    onClick = { authViewModel.signOut() },
    modifier = Modifier.align(Alignment.TopEnd)
) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
        contentDescription = "Logout",
        tint = MaterialTheme.colorScheme.error
    )
}
```

## Additional Notes

- This screen provides a clean and user-friendly interface for managing user profiles.
- The use of `Card` and `IconButton` components ensures a consistent and modern design.
- Proper navigation and state management are facilitated through `navController` and `authViewModel`.
- Profile details and actions like editing and logging out follow the common Material Design guidelines for better user experience.