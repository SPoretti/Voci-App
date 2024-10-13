package com.example.vociapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.vociapp.ui.components.BottomBar
import com.example.vociapp.ui.navigation.NavGraph
import com.example.vociapp.ui.navigation.currentRoute
import com.example.vociapp.ui.theme.VociAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // autoGenerated - onCreate : called when the activity is first created
        super.onCreate(savedInstanceState)
        // autoGenerated - enableEdgeToEdge() : enables edge-to-edge mode for the activity
        enableEdgeToEdge()
        // autoGenerated - setContent : sets the content of the activity
        setContent {
            // Theme : composable function to set theme for the app
            VociAppTheme {
                // navController : starting point of the navigation graph
                val navController = rememberNavController()
                // Scaffold : composable function to set up the main layout of the app
                // Fondamental design structure
                Scaffold(
                    bottomBar = {
                        if (currentRoute(navController) != "signIn" && currentRoute(navController) != "signUp") {
                            BottomBar(navController)
                        }
                    }
                ) { innerPadding ->
                    // NavGraph : composable function to set up the navigation graph
                    // renders the app's navigation structure
                    NavGraph(navController = navController, paddingValues = innerPadding)
                }
            }
        }
    }
}