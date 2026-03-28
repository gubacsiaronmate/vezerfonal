package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.ChangePasswordRequest
import com.smokinggunstudio.vezerfonal.network.api.changePassword
import com.smokinggunstudio.vezerfonal.network.api.requestPasswordChangeCode
import com.smokinggunstudio.vezerfonal.ui.components.DismissibleSnackBar
import com.smokinggunstudio.vezerfonal.ui.components.PasswordField
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.state.ChangePasswordState
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun ChangePasswordScreen(
    accessToken: String,
    client: HttpClient,
    onSuccess: Function,
) {
    val changePasswordState by remember { mutableStateOf(ChangePasswordState()) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val codeSentMessage = stringResource(Res.string.request_sent)

    val codeText = if (changePasswordState.passwordChangeCode == 0) ""
                   else changePasswordState.passwordChangeCode.toString()

    val canSubmit = !isLoading
        && changePasswordState.newPassword.length >= 8
        && changePasswordState.newPassword == changePasswordState.confirmPassword
        && changePasswordState.passwordChangeCode > 0

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.change_password),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            try {
                                requestPasswordChangeCode(client, accessToken)
                                snackbarMessage = codeSentMessage
                            } catch (e: Exception) {
                                snackbarMessage = e.message
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Res.string.request_code))
                }

                OutlinedTextField(
                    value = codeText,
                    onValueChange = changePasswordState::updatePasswordChangeCode,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(Res.string.code)) },
                    supportingText = { Text(stringResource(Res.string.enter_code)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                )

                PasswordField(
                    value = changePasswordState.newPassword,
                    onValueChanged = changePasswordState::updateNewPassword,
                    labelText = stringResource(Res.string.new_password),
                    supportingText = stringResource(Res.string.password_must_be_at_least_8_characters_long),
                )

                PasswordField(
                    value = changePasswordState.confirmPassword,
                    onValueChanged = changePasswordState::updateConfirmPassword,
                    labelText = stringResource(Res.string.confirm_password),
                    supportingText = stringResource(Res.string.passwords_must_match),
                )

                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            try {
                                changePassword(
                                    client,
                                    accessToken,
                                    ChangePasswordRequest(
                                        code = changePasswordState.passwordChangeCode,
                                        newPassword = changePasswordState.newPassword,
                                    )
                                )
                                snackbarMessage = null
                                onSuccess()
                            } catch (e: Exception) {
                                snackbarMessage = e.message
                                isLoading = false
                            }
                        }
                    },
                    enabled = canSubmit,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (isLoading)
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    else
                        Text(stringResource(Res.string.change_password))
                }
            }
        }

        snackbarMessage?.let { msg ->
            Box(Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
                DismissibleSnackBar(visibility = true) { Text(msg) }
            }
        }
    }
}
