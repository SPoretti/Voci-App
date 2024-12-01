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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.data.types.Gender
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.ui.viewmodels.AuthViewModel

@Composable
fun HomelessForm(
    onAddItemClick: (Homeless) -> Unit,
    navController: NavHostController,
    authViewModel: AuthViewModel,
){

    var homelessName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(Gender.Unspecified) }
    var homelessLocation by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isNavigatingBack by remember { mutableStateOf(false) }
    var isAddingHomeless by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showDialog = false
        isNavigatingBack = false
        isAddingHomeless = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Aggiunta senzatetto",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = homelessName,
            onValueChange = { homelessName = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        //TODO(): birth date picker with fish's DatePicker
//        DatePicker()
//
//        Spacer(modifier = Modifier.height(16.dp))

        //Temporary
        OutlinedTextField(
            value = homelessLocation,
            onValueChange = { homelessLocation = it },
            label = { Text("Luogo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        GenderSelector(
            selectedGender = selectedGender,
            onGenderSelected = { selectedGender = it }
        )

//        DropdownTextField(
//            value = selectedGender.name,
//            onValueChange = { selectedGender = Gender.valueOf(it) },
//            options = Gender.entries.map { it.name },
//            label = "Sesso",
//            modifier = Modifier.fillMaxWidth()
//        )

        Spacer(modifier = Modifier.height(16.dp))

//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier.fillMaxWidth()
//        ){
//            OutlinedButton(
//                onClick = {
//                    isNavigatingBack = true
//                    navController.popBackStack()
//                },
//                enabled = !isNavigatingBack,
//                colors = ButtonDefaults.outlinedButtonColors(
//                    containerColor = Color.Transparent,
//                    contentColor = MaterialTheme.colorScheme.onBackground,
//                ),
//                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
//            ) {
//                Text("Annulla")
//            }
//
//
//
//            Button(
//                onClick = {
//                    isAddingHomeless = true
//                    val newHomeless = Homeless(
//                        name = homelessName,
//                        gender = selectedGender,
//                        location = homelessLocation,
//                    )
//                    onAddItemClick(newHomeless)
//                },
//                enabled =
//                    !isAddingHomeless and
//                    homelessName.isNotEmpty() and
//                    homelessLocation.isNotEmpty(),
//            ) {
//                Text("Aggiungi")
//            }
//        }
    }
}