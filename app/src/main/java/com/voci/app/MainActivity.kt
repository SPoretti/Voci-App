package com.voci.app

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
import com.google.firebase.FirebaseApp
import com.voci.app.data.util.AuthState
import com.voci.app.data.util.NetworkConnectivityListener
import com.voci.app.di.LocalServiceLocator
import com.voci.app.di.ServiceLocator
import com.voci.app.ui.components.core.BottomBar
import com.voci.app.ui.navigation.NavGraph
import com.voci.app.ui.navigation.currentRoute
import com.voci.app.ui.theme.VociAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    // Declare the network connectivity listener
    private lateinit var networkConnectivityListener: NetworkConnectivityListener
    // Declare the location permission launcher
    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("LocationPermission", "Permission granted")
            } else {
                Log.d("LocationPermission", "Permission denied")
            }
        }
    // Override the onCreate method to set the content and start monitoring connectivity
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the network connectivity listener
        networkConnectivityListener = NetworkConnectivityListener(applicationContext)
        networkConnectivityListener.startMonitoring()
        // Delay the location permission request by 1 second
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if the location permission is granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // Request the location permission
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                Log.d("LocationPermission", "Permission already granted")
            }
        }, 1000)
        // Enable edge-to-edge mode
        enableEdgeToEdge()
        // Set the content of the activity
        setContent {
            // Initialize the service locator
            val serviceLocator = remember {
                val firestore = FirebaseFirestore.getInstance()
                ServiceLocator.initialize(applicationContext, firestore)
                ServiceLocator.getInstance()
            }
            // Provide the service locator to the composition
            CompositionLocalProvider(LocalServiceLocator provides serviceLocator) {
                // Material Theme Context
                VociAppTheme {
                    // Navigation Controller
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }
                    // Scaffold with BottomBar
                    Scaffold(
                        bottomBar = {
                            // Check if the current route is not in the list of routes that should not show the BottomBar
                            if (currentRoute(navController) !in listOf("signIn", "signUp", "emailVerification", "completeSignUp", "forgotPassword")) {
                                BottomBar(navController)
                            }
                        },
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        // Authentication State
                        val authState by serviceLocator.obtainAuthViewModel().authState.collectAsState()
                        // If the user is not authenticated, navigate to the SignIn screen
                        LaunchedEffect(authState) {
                            if (authState == AuthState.Unauthenticated) {
                                navController.navigate("signIn") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                        // Navigation Graph
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
