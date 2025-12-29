package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.DismissibleSnackBar
import com.smokinggunstudio.vezerfonal.ui.components.PasswordField
import com.smokinggunstudio.vezerfonal.ui.state.ChangePasswordState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun ChangePasswordScreen() {
    val changePasswordState by remember { mutableStateOf(ChangePasswordState()) }
    val scope = rememberCoroutineScope()
    var isSnackBarVisible by remember { mutableStateOf(false) }
    Box {
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
                    value = changePasswordState.passwordChangeCode.toString(),
                    onValueChange = changePasswordState::updatePasswordChangeCode,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    supportingText = { Text(stringResource(Res.string.enter_code)) },
                    label = {
                        Text(
                            stringResource(Res.string.code),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                PasswordField(
                    value = changePasswordState.newPassword,
                    onValueChanged = changePasswordState::updateNewPassword,
                    labelText = stringResource(Res.string.new_password),
                    supportingText = stringResource(Res.string.password_must_be_at_least_8_characters_long)
                )
                PasswordField(
                    value = changePasswordState.confirmPassword,
                    onValueChanged = changePasswordState::updateConfirmPassword,
                    labelText = stringResource(Res.string.confirm_password),
                    supportingText = stringResource(Res.string.passwords_must_match)
                )
                Button(
                    onClick = {
                        scope.launch{
                            isSnackBarVisible = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(stringResource(Res.string.change_password)) }
            }
        }
        if (isSnackBarVisible) DismissibleSnackBar(isSnackBarVisible) { Text("Yes it goons.") }
    }
}