package com.example.vociapp.ui.screens.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.ui.components.RequestForm
import com.example.vociapp.ui.viewmodels.AuthViewModel
import com.example.vociapp.ui.viewmodels.RequestViewModel

@Composable
fun AddRequestScreen(
    navController: NavHostController,
    requestViewModel: RequestViewModel,
    authViewModel: AuthViewModel,

    ) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ){
        RequestForm(
            onAddItemClick = { request ->
                requestViewModel.addRequest(request) // Call the ViewModel's addRequest function
                navController.popBackStack() // Navigate back after adding
            },
            navController = navController,
            authViewModel = authViewModel,
        )
    }

}