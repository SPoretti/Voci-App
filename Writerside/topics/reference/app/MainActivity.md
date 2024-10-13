# Main Activity (MainActivity.kt)

## Overview

This file defines the main activity of the application, which serves as the entry point. It sets up the main UI scaffold, applies the theme, and integrates the navigation graph. The activity utilizes Jetpack Compose to define a composable layout.

## Code Explanation

### Breakdown
- **`class MainActivity : ComponentActivity()`**
   - Main entry point of the application.
   - Inherits from `ComponentActivity`.

- **`override fun onCreate(savedInstanceState: Bundle?)`**
   - Lifecycle method called when the activity is created.
   - Sets up edge-to-edge display using `enableEdgeToEdge()`.
   - Applies the overall theme using `setContent`.

- **`setContent { VociAppTheme { ... } }`**
   - Applies the custom theme for the app.
   - Sets up the composable content within the theme.

- **`val navController = rememberNavController()`**
   - Initializes the `NavController` to manage navigation between composables.
   - `rememberNavController` ensures that the controller state is remembered across recompositions.

- **[`Scaffold`](https://developer.android.com/reference/kotlin/androidx/compose/material3/Scaffold)**
   - Provides the basic material design visual layout structure.
   - Parameters:
      - `bottomBar`: Sets the bottom bar component if the current route is not `signIn` or `signUp`.
      - `content`: Lambda function that sets up the `NavGraph` and passes padding values to ensure consistent layout spacing.

- **`currentRoute(navController)`**
   - Helper function to retrieve the current navigation route.
   - Used to conditionally render the `BottomBar`.

- **`NavGraph(navController = navController, paddingValues = innerPadding)`**
   - Integrates the navigation graph into the main activity.
   - Passes the `NavController` and padding values to manage navigation and layout spacing.

## Related Files

- **[NavGraph.kt](NavGraph.md):** Defines the navigation graph and individual routes for each screen.
- **[Screens.kt](Screens.md):** Defines the screen routes and related properties such as `route`, `title`, and `icon`.
- **[BottomBar.kt](BottomBar.md):** Bottom navigation component used to navigate between different sections of the app.

## Usage

The `MainActivity` is responsible for setting up the primary structure and navigation of the app. Here's a simplified overview of how it integrates different components:

### Theme Setup
Applies the custom theme to the entire composable layout:

```kotlin
setContent {
    VociAppTheme {
        // Composable content...
    }
}
```

### Navigation Setup
Initializes the `NavController` and sets up the navigation graph wrapped within the `Scaffold`:

```kotlin
val navController = rememberNavController()
Scaffold(
    bottomBar = {
        if (currentRoute(navController) != "signIn" && currentRoute(navController) != "signUp") {
            BottomBar(navController)
        }
    }
) { innerPadding ->
    NavGraph(navController = navController, paddingValues = innerPadding)
}
```

### Bottom Navigation
Conditionally renders the `BottomBar` based on the current route, ensuring it's not displayed on `signIn` and `signUp` screens.

## Additional Notes

- The main activity orchestrates the setup of theme, scaffold, navigation graph, and bottom bar.
- Edge-to-edge display is enabled for a more immersive user experience.
- Proper initialization and usage of `NavController` ensure smooth navigation transitions throughout the app.
- Conditional rendering of the `BottomBar` enhances the user experience by providing relevant navigation options based on the current screen.