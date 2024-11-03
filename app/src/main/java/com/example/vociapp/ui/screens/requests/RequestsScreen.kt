package com.example.vociapp.ui.screens.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vociapp.ui.navigation.Screens
import com.example.vociapp.ui.viewmodels.AuthViewModel

@Composable
fun RequestsScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ){
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Position inner Box
                .padding(16.dp) // Optional padding
        ){
            Button(
                onClick = { navController.navigate(Screens.AddRequest.route) },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            ) {
                Text(text = "Aggiungi")

            }
        }
    }
}
/*
fun addItemToFirestore(item: Item) {
    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
    db.collection("items") // Replace "items" with your collection name
        .add(item)
        .addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
}

fun getItemsFromFirestore(onItemsReceived: (List<Item>) -> Unit) {
    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
    db.collection("items")
        .get()
        .addOnSuccessListener { querySnapshot ->
            val items = querySnapshot.toObjects(Item::class.java)
            onItemsReceived(items)
        }
        .addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents.", exception)
        }
}*/