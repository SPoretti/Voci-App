# Screens (Screens.kt)

## Overview

This file defines the screen routes and their associated data for navigation within the application. It uses a sealed class to represent different screens and provides a central place to manage navigation destinations.

## Code Explanation

```kotlin
package com.example.vociapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screens("home", title = "Home", icon = Icons.Filled.Home)
    object UserProfile : Screens("userProfile", title = "User Profile", icon = Icons.Filled.Person)
}
```

**Breakdown:**

1. **Sealed Class `Screens`:**
    - A sealed class is used to represent the different screens in the application. This ensures that all possible screens are defined within this class and prevents the creation of arbitrary screen types.
2. **Screen Objects:**
    - Each screen is defined as an object within the `Screens` sealed class.
    - Each screen object has the following properties:
        - `route`: A string representing the unique route for the screen. This is used by the navigation system to identify and navigate to the screen.
        - `title`: A string representing the title of the screen, often displayed in the UI (e.g., in the top bar or bottom navigation).
        - `icon`: An `ImageVector` representing the icon associated with the screen, often used in navigation elements like bottom navigation bars.
3. **Example Screens:**
    - `Home`: Represents the home screen with the route "home", the title "Home", and the `Icons.Filled.Home` icon.
    - `UserProfile`: Represents the user profile screen with the route "userProfile", the title "User Profile", and the `Icons.Filled.Person` icon.

## Usage

The screen routes defined in `Screens.kt` are used in the `NavGraph` to define navigation destinations:

```kotlin
// In NavGraph.kt

composable(route = Screens.Home.route) {
    HomeScreen()
} // Defines the Home screen route

composable(route = Screens.UserProfile.route) {
    UserProfileScreen()
} // Defines the UserProfile screen route
```

## Related Files

- **[NavGraph.kt](./NavGraph.kt):** Uses the screen routes defined in `Screens.kt` to set up navigation destinations.
- **[HomeScreen.kt](../screens/HomeScreen.kt):** The UI and logic for the Home screen, associated with `Screens.Home`.
- **[UserProfileScreen.kt](../screens/UserProfileScreen.kt):** The UI and logic for the User Profile screen, associated with `Screens.UserProfile`.

## Additional Notes

- Using a sealed class for screen routes provides type safety and ensures all possible screens are defined in a central location.
- The `route` property is crucial for identifying and navigating to screens within the navigation graph.
- The `title` and `icon` properties can be used to enhance the UI and make navigation more intuitive for users.
- As you add more screens to your application, you should add corresponding screen objects to the `Screens` sealed class.