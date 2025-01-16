package com.voci.app.ui.screens.updates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.voci.app.ui.components.core.DismissButton
import com.voci.app.ui.components.updates.StatusButtonData
import com.voci.app.ui.components.updates.UpdateButton

@Composable
fun UpdateAddScreen(
    navController: NavHostController,   // Navigation controller for navigating between screens
    homelessId: String                  // ID of the homeless being updated
) {
    //----- Region: Data Initialization -----
    val buttonsData = listOf(
        StatusButtonData("Buone Condizioni", Color.Green, "Green"),
        StatusButtonData("Problematiche Segnalate", Color.Yellow, "Yellow"),
        StatusButtonData("Condizioni Critiche", Color.Red, "Red"),
        StatusButtonData("Non trovato", Color.Gray, "Gray")
    )
    //----- Region: View Composition -----
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Scegli uno stato per il tuo aggiornamento:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                // Buttons
                buttonsData.forEach { buttonData ->
                    UpdateButton(buttonData, homelessId, navController)
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Cancel Button
                DismissButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.width(200.dp)
                )
            }
        }
    }
}