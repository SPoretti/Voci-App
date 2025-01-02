package com.example.vociapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.vociapp.data.types.AuthState
import com.example.vociapp.data.util.NetworkConnectivityListener
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.di.ServiceLocator
import com.example.vociapp.ui.components.BottomBar
import com.example.vociapp.ui.navigation.NavGraph
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.navigation.currentRoute
import com.example.vociapp.ui.theme.VociAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private lateinit var networkConnectivityListener: NetworkConnectivityListener

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, proceed with notification setup
                Log.d("NotificationPermission", "Permission granted")
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                Log.d("NotificationPermission", "Permission denied")
            }
        }

    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("LocationPermission", "Permission granted")
                // You can now access location data
            } else {
                Log.d("LocationPermission", "Permission denied")
                // Handle permission denial (e.g., show a message)
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkConnectivityListener = NetworkConnectivityListener(applicationContext)
        networkConnectivityListener.startMonitoring()
        Handler(Looper.getMainLooper()).postDelayed({
            // Check and request notification permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Permission already granted
                Log.d("NotificationPermission", "Permission already granted")
            }
        }, 1000) // Delay for 1 second

        Handler(Looper.getMainLooper()).postDelayed({
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                Log.d("LocationPermission", "Permission already granted")
            }
        }, 1000) // Delay for 1 second

        enableEdgeToEdge()
        setContent {
            val serviceLocator = remember {
                val firestore = FirebaseFirestore.getInstance()
                ServiceLocator.initialize(applicationContext, firestore)
                ServiceLocator.getInstance()
            }

            CompositionLocalProvider(LocalServiceLocator provides serviceLocator) {
                VociAppTheme {
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }
                    Scaffold(
                        bottomBar = {
                            if (currentRoute(navController) !in listOf("signIn", "signUp", "emailVerification", "completeSignUp", "forgotPassword")) {
                                BottomBar(navController)
                            }
                        },
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->

                        val authState by serviceLocator.obtainAuthViewModel().authState.collectAsState()

                        LaunchedEffect(authState) {
                            when (authState) {
                                is AuthState.Authenticated -> {
                                    val user = (authState as AuthState.Authenticated).user
                                    if (user.isEmailVerified) {
                                        navController.navigate(Screens.Home.route)
                                    }
                                    else{
                                        navController.navigate(Screens.EmailVerification.route)
                                    }
                                }
                                is AuthState.Unauthenticated -> {
                                    navController.navigate(Screens.SignIn.route) {
                                        popUpTo(0) { inclusive = true }
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
    override fun onDestroy() {
        super.onDestroy()

        // Stop monitoring connectivity when the activity is destroyed
        networkConnectivityListener.stopMonitoring()
    }
}
