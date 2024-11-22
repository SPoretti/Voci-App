package com.example.vociapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.repository.RequestRepository
import com.example.vociapp.ui.screens.home.HomeScreen
import com.example.vociapp.ui.screens.userProfile.UserProfileScreen
import com.example.vociapp.ui.screens.userProfile.UpdateUserProfileScreen
import com.example.vociapp.ui.screens.auth.SignInScreen
import com.example.vociapp.ui.screens.auth.SignUpScreen
import com.example.vociapp.ui.screens.requests.AddRequestScreen
import com.example.vociapp.ui.screens.requests.RequestsScreen
import com.example.vociapp.ui.viewmodels.AuthViewModel
import com.example.vociapp.ui.viewmodels.AuthState
import com.example.vociapp.ui.viewmodels.RequestViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    val firestore = remember { FirebaseFirestore.getInstance() }
    val firestoreDataSource = remember { FirestoreDataSource(firestore) }
    val requestRepository = remember { RequestRepository(firestoreDataSource) }
    val authViewModel = remember { AuthViewModel() }
    val authState by authViewModel.authState.collectAsState()

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
        composable(route = Screens.Home.route) { HomeScreen(navController, authViewModel) }
        composable(route = Screens.SignIn.route) { SignInScreen(navController, authViewModel) }
        composable(route = Screens.SignUp.route) { SignUpScreen(navController, authViewModel) }
        composable(route = Screens.Requests.route) {
            val viewModel = remember { RequestViewModel(requestRepository) }
            RequestsScreen(navController, viewModel)
        }
        composable(route = Screens.AddRequest.route) {
            val requestViewModel = remember { RequestViewModel(requestRepository) }
            val authViewModel = remember { AuthViewModel() }
            AddRequestScreen(navController, requestViewModel, authViewModel)
        }
        composable(route = Screens.UserProfile.route) { UserProfileScreen(navController, authViewModel) }
        composable(route = Screens.UpdateUserProfile.route) { UpdateUserProfileScreen(navController, authViewModel) }
    }
}



@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
