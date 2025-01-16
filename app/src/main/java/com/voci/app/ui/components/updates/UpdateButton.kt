package com.voci.app.ui.components.updates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.voci.app.ui.components.core.hapticFeedback

@Composable
fun UpdateButton(
    buttonData: StatusButtonData,       // Data for the button
    homelessId: String,                 // ID of the homeless being updated
    navController: NavHostController    // Navigation controller for navigating between screens
) {
    //--- Region: View Composition -----
    OutlinedButton(
        // Navigate to the corresponding screen based on the button data
        onClick = { navController.navigate("UpdateAddFormScreen/${buttonData.route}/$homelessId") },
        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
            .hapticFeedback(),
        border = BorderStroke(
            width = 2.dp,
            color = buttonData.color,
        ),
        // Set the button's color and text based on the button data
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = buttonData.color.copy(alpha = 0.8f),
            contentColor = Color.Black
        )
    ) {
        Text(text = buttonData.text)
    }
}