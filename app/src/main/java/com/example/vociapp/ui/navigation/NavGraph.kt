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
import com.example.vociapp.ui.components.updates.ButtonOption
import com.example.vociapp.ui.screens.auth.SignInScreen
import com.example.vociapp.ui.screens.auth.SignUpScreen
import com.example.vociapp.ui.screens.home.HomeScreen
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
    navController: NavHostController,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState
) {

    NavHost(
        navController = navController,
        startDestination = Screens.SignIn.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        // Sign in and up screens
        composable(route = Screens.SignIn.route) { SignInScreen(navController) }
        composable(route = Screens.SignUp.route) { SignUpScreen(navController) }

        // Home screen
        composable(
            route = Screens.Home.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(600),
                    initialOffsetX = { -it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(600),
                    targetOffsetX = { -it }
                )
            }
        ) { HomeScreen(navController, snackbarHostState) }

        // Requests screens
        composable(
            route = Screens.Requests.route,
            enterTransition = {
                // Slide in from the right if coming from Home
                // Slide in from the left if coming from UserProfile
                when (initialState.destination.route) {
                    Screens.Home.route -> slideInHorizontally(animationSpec = tween(600), initialOffsetX = { it })
                    Screens.UserProfile.route -> slideInHorizontally(animationSpec = tween(600), initialOffsetX = { -it })
                    else -> null
                }
            },
            exitTransition = {
                // Slide out to the left if going to Home
                // Slide out to the right if going to UserProfile
                when (targetState.destination.route) {
                    Screens.Home.route -> slideOutHorizontally(animationSpec = tween(600), targetOffsetX = { it })
                    Screens.UserProfile.route -> slideOutHorizontally(animationSpec = tween(600), targetOffsetX = { -it })
                    else -> null
                }
            }
        ) { RequestsScreen(navController, snackbarHostState) }

        composable(route = Screens.RequestsHistory.route) { RequestsHistoryScreen(navController) }

        composable(
            route = "RequestDetailsScreen/{requestId}",
            arguments = listOf(navArgument("requestId") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(600),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(600),
                    targetOffsetX = { it }
                )
            }
        ) {
            backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId")
            RequestDetailsScreen(requestId.toString(), navController)
        }

        // Profile screens
        composable(
            route = Screens.UserProfile.route,
            enterTransition = {
                slideInHorizontally(animationSpec = tween(600), initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(600), targetOffsetX = { it })
            }
        ) { UserProfileScreen(navController) }

        composable(route = Screens.UpdateUserProfile.route) { UpdateUserProfileScreen(navController) }

        composable(
            route = "ProfileVolontario/{creatorId}",
            arguments = listOf(navArgument("creatorId") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(600),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(600),
                    targetOffsetX = { it }
                )
            }
        ) { backStackEntry ->
            val creatorId = backStackEntry.arguments?.getString("creatorId")
            ProfileVolunteerScreen(creatorId)
        }

        composable(
            route = "ProfileHomeless/{homelessId}",
            arguments = listOf(navArgument("homelessId") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(600),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(600),
                    targetOffsetX = { it }
                )
            }
        ) { backStackEntry ->
            val homelessId = backStackEntry.arguments?.getString("homelessId")
            ProfileHomelessScreen(homelessId)
        }

        // Updates Screens

        composable(
            route = "UpdatesAddScreen/{homelessId}",
            arguments = listOf(navArgument("homelessId") { type = NavType.StringType }),
            enterTransition = {
                slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { it }
                )
            },
            exitTransition = {
                slideOutVertically(
                    animationSpec = tween(600),
                    targetOffsetY = { it }
                )
            }
        ) {
                backStackEntry ->
            val homelessId = backStackEntry.arguments?.getString("homelessId")
            UpdateAddScreen(navController, homelessId.toString())
        }

        composable(
            route = "UpdateAddFormScreen/{buttonOption}/{homelessId}", // Add homelessId to route
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
            val homelessId = backStackEntry.arguments?.getString("homelessId") ?: "" // Get homelessId

            UpdateAddFormScreen(navController, buttonOption, homelessId) // Pass homelessId to UpdateAddFormScreen
        }

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
