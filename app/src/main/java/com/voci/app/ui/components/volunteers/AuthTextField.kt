package com.voci.app.ui.components.volunteers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = {},
    colors: TextFieldColors =
        OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            cursorColor = MaterialTheme.colorScheme.primary,
        ),
    isLoggingIn: Boolean = true
) {
    OutlinedTextField(
        value = value.trim(),
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.isNotBlank()) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        placeholder = { Text(placeholder) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = if (value.isNotEmpty() || isLoggingIn) {
            colors
        } else {
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.error,
                unfocusedBorderColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.error
            )
        },
        trailingIcon = if (isPassword) trailingIcon else null,
        visualTransformation =
            if (isPassword) {
                if (!isPasswordVisible)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None
            }
            else
                VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
        singleLine = true
    )
}