package com.example.vociapp.ui.components.homeless

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCommentDialog(
    onDismiss: () -> Unit,
    onAdd: (comment: String, date: String, volunteerName: String) -> Unit
) {
//    var comment by remember { mutableStateOf("") }
//    val currentDate = remember {
//        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        formatter.format(Date())
//    }
//    val serviceLocator = LocalServiceLocator.current
//    val authViewModel = serviceLocator.getAuthViewModel()
//    var volunteerName = authViewModel.getCurrentUserProfile()
//        ?.displayName ?: "Unknown Volunteer"
//    var isAddingComment by remember { mutableStateOf(false) }
//
//    AlertDialog(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        onDismissRequest = { onDismiss() },
//        properties = DialogProperties(usePlatformDefaultWidth = false),
//        shape = RoundedCornerShape(16.dp),
//        title = { Text("Aggiungi commento") },
//        text = {
//            Column(
//                modifier = Modifier
//                    .verticalScroll(rememberScrollState())
//                    .padding(16.dp),
//            ) {
//                OutlinedTextField(
//                    value = comment,
//                    onValueChange = { comment = it },
//                    label = { Text("Commento") },
//                )
//            }
//        },
//        confirmButton = {
//            Button(
//                onClick = {
//                    isAddingComment = true
//                    onAdd(comment, currentDate, volunteerName)
//                },
//            ) {
//                Text("Aggiungi")
//            }
//        },
//        dismissButton = {
//            OutlinedButton(
//                onClick = { onDismiss() },
//                colors = ButtonDefaults.outlinedButtonColors(
//                    containerColor = Color.Transparent,
//                    contentColor = MaterialTheme.colorScheme.onBackground,
//                ),
//                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
//            ) {
//                Text("Annulla")
//            }
//        },
//        containerColor = MaterialTheme.colorScheme.background,
//        textContentColor = MaterialTheme.colorScheme.onBackground,
//    )
}