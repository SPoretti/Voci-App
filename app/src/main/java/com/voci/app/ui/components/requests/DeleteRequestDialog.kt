package com.voci.app.ui.components.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.voci.app.data.local.database.Request
import com.voci.app.di.LocalServiceLocator
import com.voci.app.ui.components.core.ConfirmButton
import com.voci.app.ui.components.core.DismissButton

@Composable
fun DeleteRequestDialog(
    request: Request,
    onDismiss: () -> Unit,
    navController: NavHostController
) {
    val serviceLocator = LocalServiceLocator.current
    val requestViewModel = serviceLocator.obtainRequestViewModel()

    AlertDialog(
        // Called when the user tries to dismiss the Dialog by pressing the back button.
        // This is not called when the dismiss button is clicked.
        onDismissRequest = {
            onDismiss()
        },
        // Style
        properties = DialogProperties(usePlatformDefaultWidth = true),
//        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        textContentColor = MaterialTheme.colorScheme.onBackground,
        // Title of the dialog
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Elimina Richiesta",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        },
        // Content
        text = {
            Row {
                Text(text = "Sei sicuro di voler eliminare la richiesta?")
            }
        },
        confirmButton = {
            ConfirmButton(
                onClick = {
                    requestViewModel.deleteRequest(request)
                    navController.navigate("requestsHistory")
                    onDismiss()
                },
                text = "Elimina"
            )
        },
        dismissButton = {
            DismissButton(
                onClick = {
                    onDismiss()
                }
            )
        }
    )
}