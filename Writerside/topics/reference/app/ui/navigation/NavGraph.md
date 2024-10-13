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
   - TODO.
- **`val authState by authViewModel.authState.collectAsState()`**
   - TODO.
- **`LaunchedEffect(authState)`**
   - The LaunchedEffect 
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
- **`composable(route = Screens.Example.route) { ExampleScreen(navController) }` functions:**
   - Inside the `NavHost` scope, `composable` functions define routes for each screen.
   - List:
     - `HomeScreen`
     - `SignInScreen`
     - `SignUpScreen`
     - `UserProfileScreen`
     - `UpdateUserProfileScreen`

- **Authentication Logic:**
   - The `LaunchedEffect` block within `NavGraph` observes the authentication state and navigates to the appropriate screen based on whether the user is authenticated or not.
   - The `authState` is managed by the `AuthViewModel` and can be `Authenticated`, `Unauthenticated`, or `Uninitialized`.

- **Helper function:**
   - `@Composable fun currentRoute(navController: NavHostController): String?`
      - This helper function retrieves the current route from anywhere inside the navigation graph.
      - It uses the passed in `navController` and `currentBackStackEntryAsState()` to get the current route and can be leveraged by UI components like the BottomBar for navigation state awareness.

## Related Files

- **[Screens.kt](Screens.md):** Defines the screen routes and related properties such as `route`, `title`, and `icon`.
- **[HomeScreen.kt](../screens/HomeScreen.kt):** Defines the UI and logic for the Home screen.
- **[UserProfileScreen.kt](../screens/UserProfileScreen.kt):** Defines the UI and logic for the User Profile screen.

## Usage

The `NavGraph` setup would typically be called from your main activity's `setContent` block to establish the structure of navigation in your app:

```kotlin
import androidx.navigation.compose.rememberNavController
import com.example.vociapp.ui.navigation.NavGraph

val navController = rememberNavController()
NavGraph(navController = navController, paddingValues = innerPadding)
```

## Additional Notes

- The navigation graph organizes the flow of screens within your application.
- Each screen is represented by a `composable` route, allowing easy additions and modifications of navigation paths.
- Navigation arguments can be used to pass data between different screens by modifying the `Screens.kt` definitions.
- The `NavHostController` facilitates navigation actions like moving between screens and managing the back stack.
- You can implement custom transitions and animations to enhance the navigation experience (refer to Navigation component documentation for more details).