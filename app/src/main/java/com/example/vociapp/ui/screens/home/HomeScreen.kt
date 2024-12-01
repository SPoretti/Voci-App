package com.example.vociapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.ui.components.SearchBar
import com.example.vociapp.ui.viewmodels.AuthViewModel
import com.example.vociapp.R

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {

    val userProfile = authViewModel.getCurrentUserProfile()

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(
            painter = painterResource(R.drawable.voci_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        userProfile?.let { profile ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Ciao, ${profile.displayName}!",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 0.dp,
                        bottom = 16.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        onSearch = { /* TODO() Handle search query */ },
                        placeholderText = "Cerca..."
                    )

                }
            }
        }
    }
}



