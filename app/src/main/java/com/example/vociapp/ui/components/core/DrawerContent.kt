package com.example.vociapp.ui.components.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.R

@Composable
fun DrawerContent(
    navController: NavHostController    // Navigation controller for navigating between screens
) {
    //----- Region: View Composition -----
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                // Logo
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.voci_logo),
                        contentDescription = "Logo VoCi",
                        modifier = Modifier.size(128.dp)
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                // Navigation links
                NavigationLink(text = "Home", icon = Icons.Default.Home) {
                    navController.navigate("home")
                }
                NavigationLink(text = "Mappa Senzatetto", icon = Icons.Default.Map) {
                    navController.navigate("HomelessesMap/")
                }
//                NavigationLink(text = "API TESTING", icon = Icons.Default.Api) {
//                    navController.navigate("apiTesting")
//                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {}
    }
}
