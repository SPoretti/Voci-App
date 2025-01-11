package com.example.vociapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.vociapp.data.util.Resource
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.core.CustomFAB
import com.example.vociapp.ui.components.homeless.AddLocationSearchbar
import com.example.vociapp.ui.components.maps.MultiPointMap
import com.example.vociapp.ui.components.maps.SearchBox
import com.mapbox.geojson.Point

@Composable
fun ApiTesting(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    // Thias: 9.033408, 45.527823
    // Milano: 9.19, 45.4642
    SearchBox(
        onConfirmLocation = {

        }
    )
}