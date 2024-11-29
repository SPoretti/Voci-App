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
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VociAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        if (currentRoute(navController) != "signIn" && currentRoute(navController) != "signUp") {
                            BottomBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavGraph(navController = navController, paddingValues = innerPadding)
                }
            }
        }

    }
}
