# MainActivity

## Overview

This file contains the `MainActivity` class, which is the entry point of the application. It is responsible for setting up the main UI and handling the navigation between different screens.

## Code Explanation

```kotlin
package com.example.vociapp // Package declaration

// Imports for necessary classes and components
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.vociapp.ui.components.BottomBar
import com.example.vociapp.ui.navigation.NavGraph
import com.example.vociapp.ui.theme.VociAppTheme

class MainActivity : ComponentActivity() { // Class declaration extending ComponentActivity
    override fun onCreate(savedInstanceState: Bundle?) { // onCreate method, called when activity is created
        super.onCreate(savedInstanceState) // Call to superclass onCreate
        enableEdgeToEdge() // Enables edge-to-edge display
        setContent { // Sets content of the activity using Jetpack Compose
            VociAppTheme { // Applies the app's theme
                val navController = rememberNavController() // Creates a NavHostController for navigation
                Scaffold( // Creates a Scaffold layout with a bottom bar
                    bottomBar = { BottomBar(navController) } // Sets the BottomBar component
                ) { innerPadding -> // Provides padding values for the content
                    NavGraph(navController = navController, paddingValues = innerPadding) // Sets up the navigation graph
                }
            }
        }
    }
}
```

**Breakdown:**

1. **Package Declaration:** `package com.example.vociapp` defines the package the class belongs to.
2. **Imports:** The `import` statements bring in necessary classes and components from various libraries, such as Jetpack Compose and Navigation.
3. **Class Declaration:** `class MainActivity : ComponentActivity()` declares the `MainActivity` class, inheriting from `ComponentActivity`, which is the base class for activities in Android.
4. **onCreate() method:** This method is called when the activity is first created. It's responsible for initializing the UI and setting up the navigation.
   - `super.onCreate(savedInstanceState)` calls the superclass's `onCreate()` method to perform default initialization.
   - `enableEdgeToEdge()` enables edge-to-edge display for the activity, making the content extend to the edges of the screen.
   - `setContent { ... }` sets the content of the activity using Jetpack Compose.
   - `VociAppTheme { ... }` applies the app's custom theme.
   - `val navController = rememberNavController()` creates a `NavHostController` to manage navigation between screens.
   - `Scaffold { ... }` creates a Scaffold layout, providing a structure for the app's UI with a bottom bar.
   - `bottomBar = { BottomBar(navController) }` sets the `BottomBar` component as the bottom bar of the Scaffold.
   - `NavGraph(navController = navController, paddingValues = innerPadding)` sets up the navigation graph, defining the screens and their routes.

## Related Files

- **[BottomBar.kt](../ui/components/BottomBar.kt):** Defines the bottom navigation bar component.
- **[NavGraph.kt](../ui/navigation/NavGraph.kt):** Sets up the navigation graph for the app.
- **[VociAppTheme.kt](../ui/theme/Theme.kt):** Defines the app's custom theme.

## Additional Notes

- `MainActivity` is the entry point of the application and is responsible for initializing the UI and navigation.
- It uses Jetpack Compose to define the UI and Navigation component to manage navigation between screens.
- The `Scaffold` component provides a structure for the UI with a bottom bar.
- The `BottomBar` component is used for navigation between screens.
- The `NavGraph` component defines the screens and their routes.
