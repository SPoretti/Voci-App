package com.example.vociapp.ui.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.model.Request

@Composable
fun RequestForm(
    onAddItemClick: (Request) -> Unit,
    navController: NavHostController

) {
    var requestTitle by remember { mutableStateOf("") }
    var requestDescription by remember { mutableStateOf("") }
    var requestHomelessID by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var isNavigatingBack by remember { mutableStateOf(false) }
    var isAddingRequest by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isNavigatingBack = false
        isAddingRequest = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = requestTitle,
            onValueChange = { requestTitle = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = requestDescription,
            onValueChange = { requestDescription = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = requestHomelessID,
            onValueChange = { requestHomelessID = it },
            label = { Text("Homeless") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownTextField(
            value = selectedOption,
            onValueChange = { selectedOption = it },
            options = listOf("Option 1", "Option 2", "Option 3"),
            label = "Select an option"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ){
            OutlinedButton(
                onClick = {
                    isNavigatingBack = true
                    navController.popBackStack()
                },
                enabled = !isNavigatingBack,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                Text("Annulla")
            }



            Button(
                onClick = {
                    isAddingRequest = true
                    val newRequest = Request(
                        title = requestTitle,
                        description = requestDescription,
                        homelessID = requestHomelessID
                    )
                    onAddItemClick(newRequest)
                },
                enabled = !isAddingRequest,
            ) {
                Text("Aggiungi")
            }
        }
    }
}