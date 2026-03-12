package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing

@Composable
fun PasswordField(
    value: String,
    labelText: String,
    supportingText: String = "",
    onValueChanged: (String) -> Unit,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(labelText, color = MaterialTheme.colorScheme.onSurface) },
        singleLine = true,
        supportingText = { Text(supportingText, color = MaterialTheme.colorScheme.onSurface) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation =
            if (isPasswordVisible)
                VisualTransformation.None
            else PasswordVisualTransformation(),
        trailingIcon = {
            val icon =
                if (isPasswordVisible)
                    Icons.Filled.VisibilityOff
                else Icons.Filled.Visibility
            
            IconButton({isPasswordVisible = !isPasswordVisible}) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}