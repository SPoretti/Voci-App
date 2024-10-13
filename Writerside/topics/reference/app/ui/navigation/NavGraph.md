# Navigation Graph (NavGraph.kt)

## Overview

This file defines the navigation graph for the application, which controls the navigation flow between different screens. It uses Jetpack Compose's Navigation component to manage the navigation stack and transitions.

## Code Explanation

### Breakdown
- **`@Composable fun NavGraph(navController: NavHostController, paddingValues: PaddingValues)`**
    - This composable function sets up the navigation graph using `NavHost`.
    - Parameters:
        - `navController`: Receives the original NavHostController created in the [Main Activity](MainActivity.md).
        - `paddingValues`: Receives the padding values that are default from the [Scaffold](https://https://developer.android.com/develop/ui/compose/components/scaffold).
- **`val authViewModel = remember { AuthViewModel() }`**
    - Initializes an instance of `AuthViewModel` using the `remember` composable for state retention across recompositions.
- **`val authState by authViewModel.authState.collectAsState()`**
    - Collects the authentication state from the `AuthViewModel` as a state value using the `collectAsState` extension function.
- **`LaunchedEffect(authState)`**
    - A side effect that runs whenever `authState` changes. Useful for performing actions such as navigation based on the authentication state.
- **`when (authState)`**
    - Defines a list of actions that depend on the state of the user authentication:
        - `AuthState.Authenticated`: The user gets routed to the Home screen.
        - `AuthState.Unauthenticated`: The user gets routed to the SignIn screen.
- **`NavHost( navController = navController, startDestination = Screens.Home.route, modifier = Modifier.padding(paddingValues) )`**
    - This function sets up the navigation graph with all the possible routes and the according [Screens](Screens.md).
    - Parameters:
        - `navController`: Receives the original NavHostController created in the [Main Activity](MainActivity.md).
        - `modifier`: Applies the padding values.
        - `startDestination` parameter specifies the initial screen to be displayed when the app launches.
- **`composable(route = Screens.Example.route) { ExampleScreen(navController) }`:**
    - Inside the `NavHost` scope, `composable` functions define routes for each screen.
    - List:
        - `HomeScreen`
        - `SignInScreen`
        - `SignUpScreen`
        - `UserProfileScreen`
        - `UpdateUserProfileScreen`
- **`fun currentRoute(navController: NavHostController): String?` Helper function:**
    - This helper function retrieves the current route from anywhere inside the navigation graph.
    - It uses the passed in `navController` and `currentBackStackEntryAsState()` to get the current route that can be used in components like the [BottomBar](BottomBar.md).

## Related Files

- **[](Screens.md):** Defines the screen routes and related properties such as `route`, `title`, and `icon`.
- **[](MainActivity.md):** The MainActivity is the entry point of the app and is responsible for the creation of the navController.

## Usage

### NavGraph
The `NavGraph` setup is called from the main activity's `setContent` block to establish the structure of navigation in your app:

```kotlin
import androidx.navigation.compose.rememberNavController
import com.example.vociapp.ui.navigation.NavGraph

val navController = rememberNavController()
NavGraph(navController = navController, paddingValues = innerPadding)
```

### Rest of the app
Throughout the rest of the app we are going to use the navController parameter that is passed to every screen like this:

```kotlin
// example
navController.navigate("signIn")
```

## Additional Notes

- The navigation graph's organization allows for a clear and scalable structure for handling multiple screens.
- Proper management of the authentication state enhances user experience by directing them to the appropriate screens based on their login status.
- Remember to handle deeper navigation features or customized transitions by referring to the official Jetpack Compose Navigation documentation.