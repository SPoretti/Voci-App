# Bottom Navigation Bar (BottomBar.kt)

## Overview

This file defines the bottom navigation bar component for the application. The bottom bar allows users to navigate between different screens seamlessly. It uses Jetpack Compose's `NavigationBar` and `NavigationBarItem` components to define the layout and behavior of the navigation items.

## Code Explanation

### Breakdown
- **`@Composable fun BottomBar(navController: NavHostController)`**
   - Defines the bottom navigation bar as a composable function.
   - Parameters:
      - `navController`: The navigation controller that manages app navigation.

- **`val items = listOf(Screens.Home, Screens.UserProfile)`**
   - Specifies the list of screens (menu items) to be included in the bottom navigation bar.
   - Uses the `Screens` object to reference screen routes and properties.

- **`val currentRoute = currentRoute(navController)`**
   - Retrieves the current route using the `currentRoute` helper function.

- **`NavigationBar { ... }`**
   - Container composable for the navigation bar.
   - Iterates through the `items` list to create `NavigationBarItem` for each screen.

- **`NavigationBarItem(...)`**
   - Defines individual navigation items.
   - Parameters:
      - `icon`: Displays the icon for each screen.
      - `label`: Displays the label (title) for each screen.
      - `selected`: Boolean flag to indicate if the current item is selected, based on the current route.
      - `onClick`: Lambda function to handle navigation when an item is clicked.
         - Uses `navController.navigate(screen.route)` to navigate to the respective screen.
         - Utilizes navigation options like `popUpTo`, `launchSingleTop`, and `restoreState` for optimal navigation behavior.

## Related Files

- **[NavGraph.kt](NavGraph.md):** Defines the navigation graph and individual routes for each screen.
- **[Screens.kt](Screens.md):** Defines the screen routes and related properties such as `route`, `title`, and `icon`.
- **[MainActivity.kt](MainActivity.md):** Entry point of the app, where the `BottomBar` is conditionally rendered based on the current route.

## Usage

The `BottomBar` is conditionally displayed in the main layout of the app and provides easy navigation between key screens. Here's how it is typically integrated:

### Example Usage in MainActivity

The `BottomBar` is included in the `Scaffold` of the primary activity layout:

```kotlin
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

### Navigation Handling

The `BottomBar` handles the navigation between screens with optimal behavior, ensuring the state is preserved and navigating to new destinations is efficient:

```kotlin
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
```

## Additional Notes

- The bottom navigation bar enhances user experience by providing quick access to core screens.
- Proper handling of navigation and state restoration ensures a smooth and consistent user experience.
- `NavigationBar` and `NavigationBarItem` components from Jetpack Compose are used to create a customizable and responsive navigation interface.