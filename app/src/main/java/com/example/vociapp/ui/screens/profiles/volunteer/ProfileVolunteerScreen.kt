package com.example.vociapp.ui.screens.profiles.volunteer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.volunteers.ProfileInfoItem

@Composable
fun ProfileVolunteerScreen(creatorId: String) {
    val serviceLocator = LocalServiceLocator.current
    val volunteerViewModel = serviceLocator.obtainVolunteerViewModel()

    LaunchedEffect(key1 = creatorId) {
        volunteerViewModel.getVolunteerById(creatorId)
    }

    val volunteerResource by volunteerViewModel.specificVolunteer.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (volunteerResource) {
                    is Resource.Loading -> {
                        Text(text = "Caricamento...", style = MaterialTheme.typography.headlineMedium)
                    }

                    is Resource.Success -> {
                        val volunteer = volunteerResource.data

                        val initials =
                            "${volunteer?.name?.firstOrNull() ?: ""}${volunteer?.surname?.firstOrNull() ?: ""}".uppercase()

                        //Profile picture
                        if (volunteer != null) {
                            if (volunteer.photoUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = volunteer.photoUrl,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(130.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                ) {
                                    Text(
                                        text = initials,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 60.sp
                                    )
                                }
                            }
                        }

                        Text(
                            text = volunteer?.nickname ?: "Nickname non disponibile",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // name
                        ProfileInfoItem(
                            icon = Icons.Default.Person,
                            label = "Nome",
                            value = volunteer?.name ?: "Nome non disponibile"
                        )

                        // surname
                        ProfileInfoItem(
                            icon = Icons.Default.Person,
                            label = "Cognome",
                            value = volunteer?.surname ?: "Cognome non disponibile"
                        )

                        // email
                        ProfileInfoItem(
                            icon = Icons.Default.Email,
                            label = "Email",
                            value = volunteer?.email ?: "Email non disponibile"
                        )

                        // phone number
                        ProfileInfoItem(
                            icon = Icons.Default.Phone,
                            label = "Numero di telefono",
                            value = volunteer?.phone_number ?: "Numero di telefono non disponibile"
                        )
                    }

                    is Resource.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${(volunteerResource as Resource.Error).message}",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                }
            }
        }
    }
}


