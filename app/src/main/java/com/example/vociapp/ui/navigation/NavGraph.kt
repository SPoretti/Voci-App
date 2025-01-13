package com.example.vociapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.vociapp.ui.components.core.Screens
import com.example.vociapp.ui.components.updates.ButtonOption
import com.example.vociapp.ui.screens.ApiTesting
import com.example.vociapp.ui.screens.auth.SignInScreen
import com.example.vociapp.ui.screens.auth.SignUpScreen
import com.example.vociapp.ui.screens.home.HomeScreen
import com.example.vociapp.ui.screens.maps.HomelessesMap
import com.example.vociapp.ui.screens.profiles.homeless.ProfileHomelessScreen
import com.example.vociapp.ui.screens.profiles.userProfile.UpdateUserProfileScreen
import com.example.vociapp.ui.screens.profiles.userProfile.UserProfileScreen
import com.example.vociapp.ui.screens.profiles.volunteer.ProfileVolunteerScreen
import com.example.vociapp.ui.screens.requests.RequestDetailsScreen
import com.example.vociapp.ui.screens.requests.RequestsHistoryScreen
import com.example.vociapp.ui.screens.requests.RequestsScreen
import com.example.vociapp.ui.screens.updates.UpdateAddFormScreen
import com.example.vociapp.ui.screens.updates.UpdateAddScreen

@Composable
fun NavGraph(
    navController: NavHostController,           // Navigation controller to set up the navigation graph
    paddingValues: PaddingValues,               // Padding values for the content
    snackbarHostState: SnackbarHostState        // Snackbar host state for displaying snackbar messages
) {
    //-------------- Region: Navigation Graph Constants ---------------
    val navigationAnimationDuration = 400     // Duration of navigation animations in milliseconds

    // Navigation graph
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route,      // Starting destination
        modifier = Modifier.padding(paddingValues)  // Apply padding values to the screen
    ) {
        //--------------- Region: Navigation Graph Routes ---------------

        //---------- Home screen ----------
        composable(
            route = "home",
            enterTransition = {
                // Slide in from the left if coming from Requests
                when (initialState.destination.route) {
                    Screens.Requests.route -> slideInHorizontally(animationSpec = tween(navigationAnimationDuration), initialOffsetX = { -it })
                    else -> null
                }
            },
            exitTransition = {
                // Slide out to the right if going to Requests
                when (targetState.destination.route) {
                    Screens.Requests.route -> slideOutHorizontally(animationSpec = tween(navigationAnimationDuration), targetOffsetX = { -it })
                    else -> null
                }
            }
        ) { HomeScreen(navController, snackbarHostState) }


        //---------- User screens ----------
        composable(route = "signIn") { SignInScreen(navController) }
        composable(route = "signUp") { SignUpScreen(navController) }
        // Profile screens
        composable(
            route = "userProfile",
            enterTransition = {
                slideInHorizontally(animationSpec = tween(navigationAnimationDuration), initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(navigationAnimationDuration), targetOffsetX = { it })
            }
        ) { UserProfileScreen(navController) }
        composable(
            route = "ProfileVolontario/{creatorId}",
            arguments = listOf(navArgument("creatorId") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(navigationAnimationDuration),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(navigationAnimationDuration),
                    targetOffsetX = { it }
                )
            }
        ) { backStackEntry ->
            val creatorId = backStackEntry.arguments?.getString("creatorId")
            ProfileVolunteerScreen(creatorId)
        }
        composable(route = "updateUserProfile") { UpdateUserProfileScreen(navController) }


        //---------- Requests screens ----------
        composable(
            route = "requests",
            enterTransition = {
                // Slide in from the right if coming from Home
                when (initialState.destination.route) {
                    Screens.Home.route -> slideInHorizontally(animationSpec = tween(navigationAnimationDuration), initialOffsetX = { it })
                    else -> null
                }
            },
            exitTransition = {
                // Slide out to the left if going to Home
                when (targetState.destination.route) {
                    Screens.Home.route -> slideOutHorizontally(animationSpec = tween(navigationAnimationDuration), targetOffsetX = { it })
                    else -> null
                }
            }
        ) { RequestsScreen(navController, snackbarHostState) }
        composable(
            route = "requestsHistory",
            enterTransition = {
                slideInHorizontally(animationSpec = tween(navigationAnimationDuration), initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(navigationAnimationDuration), targetOffsetX = { it })
            }
        ) { RequestsHistoryScreen(navController) }
        composable(
            route = "RequestDetailsScreen/{requestId}",
            arguments = listOf(navArgument("requestId") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(navigationAnimationDuration),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(navigationAnimationDuration),
                    targetOffsetX = { it }
                )
            }
        ) {
            backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId")
            RequestDetailsScreen(requestId.toString(), navController)
        }


        //---------- Homeless screens ----------
        composable(
            route = "ProfileHomeless/{homelessId}",
            arguments = listOf(navArgument("homelessId") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(navigationAnimationDuration),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(navigationAnimationDuration),
                    targetOffsetX = { it }
                )
            }
        ) { backStackEntry ->
            val homelessId = backStackEntry.arguments?.getString("homelessId")
            if (homelessId != null) {
                ProfileHomelessScreen(navController, homelessId)
            }
        }


        //---------- Updates Screens ----------
        composable(
            route = "UpdatesAddScreen/{homelessId}",
            arguments = listOf(navArgument("homelessId") { type = NavType.StringType }),
            enterTransition = {
                slideInVertically(
                    animationSpec = tween(navigationAnimationDuration),
                    initialOffsetY = { it }
                )
            },
            exitTransition = {
                slideOutVertically(
                    animationSpec = tween(navigationAnimationDuration),
                    targetOffsetY = { it }
                )
            }
        ) {
                backStackEntry ->
            val homelessId = backStackEntry.arguments?.getString("homelessId")
            UpdateAddScreen(navController, homelessId.toString())
        }
        composable(
            route = "UpdateAddFormScreen/{buttonOption}/{homelessId}",
            arguments = listOf(
                navArgument("buttonOption") { type = NavType.StringType },
                navArgument("homelessId") { type = NavType.StringType }
            ),
        ) { backStackEntry ->
            val buttonOptionString = backStackEntry.arguments?.getString("buttonOption")
            val buttonOption = when (buttonOptionString) {
                "Green" -> ButtonOption.Green
                "Yellow" -> ButtonOption.Yellow
                "Red" -> ButtonOption.Red
                "Gray" -> ButtonOption.Gray
                else -> ButtonOption.Green
            }
            val homelessId = backStackEntry.arguments?.getString("homelessId") ?: ""

            UpdateAddFormScreen(navController, buttonOption, homelessId)
        }

        composable(
            route = "apiTesting",
        ) {
            ApiTesting(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        //---------- Maps Screens ----------
        composable(
            route = "HomelessesMap/{homelessId}",
            arguments = listOf(navArgument("homelessId") { type = NavType.StringType }),
            enterTransition = {
                slideInVertically(
                    animationSpec = tween(navigationAnimationDuration),
                    initialOffsetY = { it }
                )
            },
            exitTransition = {
                slideOutVertically(
                    animationSpec = tween(navigationAnimationDuration),
                    targetOffsetY = { it }
                )
            }
        ) {
                backStackEntry ->
            val homelessId = backStackEntry.arguments?.getString("homelessId")
            if (homelessId != null) {
                HomelessesMap(homelessId)
            }}
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
