# Bottom Navigation Bar (BottomBar.kt)

## Overview

This file defines the `BottomBar` composable function, which creates a bottom navigation bar for the application. It uses Material Design 3 components and provides navigation between different screens.

## Code Explanation

```kotlin
@Composable
fun BottomBar(navController:  NavHostController) { 
    val items = listOf( 
        Screens.Home, Screens.UserProfile
    ) 
    val currentRoute = currentRoute(navController)
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
```

**Breakdown:**

1. **`BottomBar` function:**
    - This composable function creates the bottom navigation bar.
    - It takes a `NavHostController` as a parameter to handle navigation actions.
2. **Navigation Items:**
    - `items`: A list of `Screens` objects representing the navigation destinations.
    - `currentRoute`: Obtains the current route using `currentRoute(navController)`.
3. **`NavigationBar`:**
    - A Material Design 3 component that displays the bottom navigation bar.
    - It iterates through the `items` list and creates a `NavigationBarItem` for each screen.
4. **`NavigationBarItem`:**
    - Represents a single item in the bottom navigation bar.
    - `icon`: Displays the icon associated with the screen.
    - `label`: Displays the title of the screen.
    - `selected`: Indicates whether the item is currently selected.
    - `onClick`: Defines the action to perform when the item is clicked, which triggers navigation to the corresponding screen.
        - `navController.navigate(screen.route)` navigates to the specified route.
        - `popUpTo` is used to navigate to the start destination of the graph, clearing the back stack.
        - `launchSingleTop` ensures that only one instance of the destination screen is created.
        - `restoreState` restores the state of the destination screen if it was previously saved.

## Related Files

- **[Screens.kt](../navigation/Screens.kt):** Defines the screen routes and their associated data.
- **[NavGraph.kt](../navigation/NavGraph.kt):** Uses the `BottomBar` to provide navigation between screens.

## Usage

The `BottomBar` is typically used within a `Scaffold` composable to provide a bottom navigation bar for the application:

## Additional Notes

- The `BottomBar` provides a user-friendly way to navigate between different screens in the application.
- It uses Material Design 3 components for a consistent and modern look and feel.
- The navigation logic ensures that the back stack is cleared when navigating to a new screen from the bottom navigation bar.
- The `currentRoute` function is used to highlight the currently selected item in the navigation bar.
