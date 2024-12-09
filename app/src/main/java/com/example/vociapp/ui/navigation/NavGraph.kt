package com.example.vociapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.vociapp.data.types.AuthState
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.screens.auth.SignInScreen
import com.example.vociapp.ui.screens.auth.SignUpScreen
import com.example.vociapp.ui.screens.home.HomeScreen
import com.example.vociapp.ui.screens.profiles.homeless.ProfileHomelessScreen
import com.example.vociapp.ui.screens.profiles.userProfile.UpdateUserProfileScreen
import com.example.vociapp.ui.screens.profiles.userProfile.UserProfileScreen
import com.example.vociapp.ui.screens.profiles.volunteer.ProfileVolunteerScreen
import com.example.vociapp.ui.screens.requests.RequestsHistoryScreen
import com.example.vociapp.ui.screens.requests.RequestsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    val serviceLocator = LocalServiceLocator.current
    val authState by serviceLocator.getAuthViewModel().authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(Screens.Home.route)
            }
            is AuthState.Unauthenticated -> {
                navController.navigate(Screens.SignIn.route)
            }

            AuthState.Uninitialized -> {
                // Handle uninitialized state if needed
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(route = Screens.Home.route) { HomeScreen() }
        composable(route = Screens.SignIn.route) { SignInScreen(navController) }
        composable(route = Screens.SignUp.route) { SignUpScreen(navController) }
        composable(route = Screens.Requests.route) { RequestsScreen(navController) }
        composable(route = Screens.RequestsHistory.route) { RequestsHistoryScreen(navController) }
        composable(route = Screens.UserProfile.route) { UserProfileScreen(navController) }
        composable(route = Screens.UpdateUserProfile.route) { UpdateUserProfileScreen(navController) }

        composable(
            route = "ProfileVolontario/{creatorId}",
            arguments = listOf(navArgument("creatorId") { type = NavType.StringType })
        ) {
            backStackEntry ->
            val creatorId = backStackEntry.arguments?.getString("creatorId")
            ProfileVolunteerScreen(creatorId)
        }
        composable(
            route = "ProfileHomeless/{homelessId}",
            arguments = listOf(navArgument("homelessId") { type = NavType.StringType })
        ) {
                backStackEntry ->
            val homelessId = backStackEntry.arguments?.getString("homelessId")
            ProfileHomelessScreen(homelessId)
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
