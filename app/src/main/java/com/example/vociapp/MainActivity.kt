package com.example.vociapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.vociapp.data.types.AuthState
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.di.ServiceLocator
import com.example.vociapp.ui.components.BottomBar
import com.example.vociapp.ui.navigation.NavGraph
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.navigation.currentRoute
import com.example.vociapp.ui.theme.VociAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val serviceLocator = remember {
                val firestore = FirebaseFirestore.getInstance()
                ServiceLocator.initialize(firestore)
                ServiceLocator.getInstance()
            }

            CompositionLocalProvider(LocalServiceLocator provides serviceLocator) {
                VociAppTheme {
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }
                    Scaffold(
                        bottomBar = {
                            if (currentRoute(navController) !in listOf("signIn", "signUp")) {
                                BottomBar(navController)
                            }
                        },
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->

                        val authState by serviceLocator.getAuthViewModel().authState.collectAsState()

                        LaunchedEffect(authState) {
                            when (authState) {
                                is AuthState.Authenticated -> {
                                    navController.navigate(Screens.Home.route)
                                }
                                is AuthState.Unauthenticated -> {
                                    navController.navigate(Screens.SignIn.route) {
                                        popUpTo(0) { inclusive = true } // Clear entire back stack
                                    }
                                }

                                AuthState.Uninitialized -> {
                                    // Handle uninitialized state if needed
                                }
                            }
                        }

                        NavGraph(
                            navController = navController,
                            paddingValues = innerPadding,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }
}
