# Navigation Graph (NavGraph.kt)

## Overview

This file defines the navigation graph for the application, which controls the navigation flow between different screens. It uses Jetpack Compose's Navigation component to manage the navigation stack and transitions.

## Code Explanation

```kotlin
package com.example.vociapp.ui.navigation

// ... IMPORTS 

@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues) {
   NavHost(
      navController = navController,
      startDestination = Screens.Home.route, // Sets the initial screen
      modifier = Modifier.padding(paddingValues) // Applies padding to the content
   ) {
      composable(route = Screens.Home.route) {
         HomeScreen(navController)
      } // Defines the Home screen route
      composable(route = Screens.UserProfile.route) {
         UserProfileScreen(navController)
      } // Defines the UserProfile screen route
      // Add more composable routes for other screens here...
   }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
   val navBackStackEntry by navController.currentBackStackEntryAsState()
   return navBackStackEntry?.destination?.route
}
```

**Breakdown:**

1. **NavGraph function:**
    - This composable function sets up the navigation graph using `NavHost`.
    - It takes a `NavHostController` and `PaddingValues` as parameters.
    - `startDestination` specifies the initial screen to be displayed.
    - `modifier` applies padding to the content of the navigation graph.
2. **Composable Routes:**
    - Inside the `NavHost` scope, `composable` functions are used to define routes for each screen.
    - Each `composable` function takes a route string as a parameter and a content lambda that defines the screen's UI.
    - The `HomeScreen` and `UserProfileScreen` composables are examples of screen content.
3. **currentRoute function:**
    - Helper function to obtain the current route from anywhere inside the navigation graph.
    - Uses the passed in navController and `currentBackStackEntryAsState()` to get the current route.
    - Used by the bottomBar for highlighting the current screen.

## Related Files

- **[Screens.kt](./Screens.kt):** Defines the screen routes and navigation arguments (if any).
- **[HomeScreen.kt](../screens/HomeScreen.kt):** Defines the UI and logic for the Home screen.
- **[UserProfileScreen.kt](../screens/UserProfileScreen.kt):** Defines the UI and logic for the User Profile screen.

## Usage

The `NavGraph` is typically used in your main activity's `setContent` block to set up the navigation structure:

```kotlin
import com.example.vociapp.ui.navigation.NavGraph
val navController = rememberNavController()
NavGraph(navController = navController, paddingValues = innerPadding)
```

## Additional Notes

- The navigation graph defines the flow of screens in your application.
- Each screen is represented by a `composable` route.
- You can add more screens and routes as needed.
- Navigation arguments can be used to pass data between screens (see `Screens.kt`).
- The `NavHostController` is used to control navigation actions.
- Custom transitions and animations can be added to enhance the navigation experience (see Navigation component documentation for more details).