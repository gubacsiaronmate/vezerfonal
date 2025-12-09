package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.state.ChangePasswordState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.*

@Composable
fun ChangePasswordScreen() {
    val changePasswordState by remember { mutableStateOf(ChangePasswordState()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = stringResource(Res.string.change_password),
            style = MaterialTheme.typography.headlineSmall
        )
        Column(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.6f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                value = changePasswordState.currentPassword,
                onValueChange = changePasswordState::updateCurrentPassword,
                label = {
                    Text(
                        stringResource(Res.string.old_password),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            OutlinedTextField(
                value = changePasswordState.newPassword,
                onValueChange = changePasswordState::updateNewPassword,
                label = {
                    Text(
                        stringResource(Res.string.new_password),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                singleLine = true,
                supportingText = { Text(stringResource(Res.string.password_must_be_at_least_8_characters_long)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            OutlinedTextField(
                value = changePasswordState.confirmPassword,
                onValueChange = changePasswordState::updateConfirmPassword,
                label = {
                    Text(
                        stringResource(Res.string.confirm_password),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                singleLine = true,
                supportingText = {
                    Text(
                        stringResource(Res.string.passwords_must_match),
                        color = MaterialTheme.colorScheme.onSurface) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(Res.string.change_password))}
        }
    }
}