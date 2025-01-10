package com.example.vociapp.ui.components.updates

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.util.Resource
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import androidx.navigation.NavHostController
import com.example.vociapp.ui.viewmodels.UpdatesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateListDialog(
    onDismiss: () -> Unit,
    updates: Resource<List<Update>>,
    navController: NavHostController,
    updateViewModel: UpdatesViewModel,
    homelessViewModel: HomelessViewModel,
) {
    AlertDialog(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        shape = RoundedCornerShape(0.dp),
        title = { Text("Lista Aggiornamenti") },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                UpdateList(
                    updates, navController, updateViewModel, homelessViewModel
                )
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                Text("Indietro")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground,
    )
}