package com.example.vociapp.ui.screens.updates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.ui.components.utils.hapticFeedback

@Composable
fun UpdateAddScreen(
    navController: NavHostController,
    homelessId: String
) {

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

                Text(
                    text = "Scegli uno stato per la tua update:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedButton(
                    onClick = {
                        navController.navigate("UpdateAddFormScreen/Green/$homelessId")
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Green,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .hapticFeedback(),
                ) {
                    Text(
                        text = "Stabile",
                        color = Color.Green
                    )
                }

                OutlinedButton(
                    onClick = {
                        navController.navigate("UpdateAddFormScreen/Yellow/$homelessId")
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Yellow,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .hapticFeedback(),
                ) {
                    Text(
                        text = "Instabile",
                        color = Color.Yellow
                    )
                }

                OutlinedButton(
                    onClick = {
                        navController.navigate("UpdateAddFormScreen/Red/$homelessId")
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Red,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .hapticFeedback(),
                ) {
                    Text(
                        text = "Negativo",
                        color = Color.Red
                    )
                }

                OutlinedButton(
                    onClick = {
                        navController.navigate("UpdateAddFormScreen/Gray/$homelessId")
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .hapticFeedback()
                    ,
                ) {
                    Text(
                        text = "Non trovato",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.width(200.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Annulla")
                }

            }
        }

    }
}