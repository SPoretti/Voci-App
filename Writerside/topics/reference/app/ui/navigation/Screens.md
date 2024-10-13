# Screens (Screens.kt)

## Overview

This file defines the screen routes and their associated data for navigation within the application. It uses a sealed class to represent different screens and provides a central place to manage navigation destinations.

## Code Explanation

### Breakdown
- **`sealed class Screens(val route: String, val title: String, val icon: ImageVector)`**
    - A sealed class is used to represent the different screens in the application. This ensures that all possible screens are defined within this class and prevents the creation of arbitrary screen types.
- **`object Home : Screens("home", title = "Home", icon = Icons.Filled.Home)`**
    - Each screen is defined as an object within the Screens sealed class.
    - Each screen object has the following properties:
        - `route`: A string representing the unique route for the screen. This is used by the navigation system to identify and navigate to the screen.
        - `title`: A string representing the title of the screen, used in the [BottomBar](BottomBar.md).
        - `icon`: An `ImageVector` representing the icon associated with the screen, used in the [BottomBar](BottomBar.md).
- **Screens list:**
    - `Home`
    - `UserProfile`
    - `UpdateUserProfile`
    - `SignIn`
    - `SignUp`

## Usage

### NavGraph

The screen routes defined in `Screens.kt` are used in the [NavGraph](NavGraph.md) to define navigation destinations:

```kotlin
// In NavGraph.kt

composable(route = Screens.Home.route) {
    HomeScreen()
} 

composable(route = Screens.UserProfile.route) {
    UserProfileScreen()
} 
```

### BottomBar

The screen `title` and `icon` are used in the [BottomBar](BottomBar.md) component to render the items of the bar:

```kotlin
// In BottomBar.kt

fun BottomBar(navController: NavHostController) {
    val items = listOf(
        Screens.Home,
        Screens.UserProfile
    )
    
    //other parts of the component

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}
```

## Related Files

- **[NavGraph](NavGraph.md):** Uses the screen `routes` defined to set up navigation destinations.
- **[BottomBar](BottomBar.md):** Uses the Screens class to render the items in the bottom bar with the `icon` and `title`.

## Additional Notes

- Using a sealed class for screen routes provides type safety and ensures all possible screens are defined in a central location.
- The `route` property is crucial for identifying and navigating to screens within the navigation graph.
- The `title` and `icon` properties are used in the UI components.