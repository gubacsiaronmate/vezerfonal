package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.network.api.disableTwoFactor
import com.smokinggunstudio.vezerfonal.network.api.enableTwoFactor
import com.smokinggunstudio.vezerfonal.network.api.requestTwoFactorCode
import com.smokinggunstudio.vezerfonal.ui.components.DismissibleSnackBar
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun TwoFactorSetupScreen(
    twoFactorEnabled: Boolean,
    accessToken: String,
    client: HttpClient,
    onBack: Function,
) {
    var isEnabled by remember { mutableStateOf(twoFactorEnabled) }
    var code by remember { mutableStateOf("") }
    var codeSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    var showDisableDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val codeAsInt = code.toIntOrNull() ?: 0
    val canEnable = !isLoading && codeSent && code.length == 6 && codeAsInt > 0
    val requestSentMessage = stringResource(Res.string.request_sent)
    val isExpanded = LocalWindowSizeInfo.current.widthClass == WindowWidthClass.Expanded

    val screenContent: @Composable ColumnScope.() -> Unit = {
        Text(
            text = stringResource(Res.string.two_factor_authentication),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = Spacing.xl),
        )

        if (isEnabled) {
            Icon(
                imageVector = Icons.Outlined.Shield,
                contentDescription = null,
                modifier = Modifier.size(48.dp).align(Alignment.CenterHorizontally),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(Spacing.md))
            Text(
                text = stringResource(Res.string.account_secure),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.height(Spacing.sm))
            Text(
                text = stringResource(Res.string.account_secure_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(Spacing.xl))
            Text(
                text = stringResource(Res.string.disable_2fa_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(Spacing.lg))
            OutlinedButton(
                onClick = { showDisableDialog = true },
                modifier = Modifier.fillMaxWidth(),
            ) { Text(stringResource(Res.string.disable_2fa)) }
        } else {
            Text(
                text = stringResource(Res.string.setup_2fa_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(Spacing.lg))
            Text(stringResource(Res.string.how_it_works), style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(Spacing.xs))
            Text(stringResource(Res.string.how_it_works_step1), style = MaterialTheme.typography.bodySmall)
            Text(stringResource(Res.string.how_it_works_step2), style = MaterialTheme.typography.bodySmall)
            Text(stringResource(Res.string.how_it_works_step3), style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(Spacing.xl))
            OutlinedButton(
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {
                            requestTwoFactorCode(client, accessToken)
                            codeSent = true
                            snackbarMessage = requestSentMessage
                        } catch (e: Exception) {
                            snackbarMessage = e.message
                        } finally { isLoading = false }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) { Text(stringResource(Res.string.request_code)) }
            Spacer(Modifier.height(Spacing.sm))
            OutlinedTextField(
                value = code,
                onValueChange = { if (it.length <= 6) code = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(Res.string.code)) },
                supportingText = { Text(stringResource(Res.string.enter_code)) },
                singleLine = true,
                enabled = codeSent,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(Spacing.sm))
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {
                            enableTwoFactor(client, accessToken, codeAsInt)
                            isEnabled = true
                            code = ""
                            codeSent = false
                        } catch (e: Exception) {
                            snackbarMessage = e.message
                        } finally { isLoading = false }
                    }
                },
                enabled = canEnable,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isLoading)
                    CircularProgressIndicator(
                        modifier = Modifier.size(Spacing.lg),
                        strokeWidth = Spacing.xs,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                else
                    Text(stringResource(Res.string.set_up_2fa))
            }
        }

        Spacer(Modifier.height(Spacing.xl))
    }

    Box(Modifier.fillMaxSize()) {
        if (isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.xl),
                contentAlignment = Alignment.TopCenter,
            ) {
                Card(
                    modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.lg)
                            .verticalScroll(rememberScrollState()),
                    ) { screenContent() }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.lg)
                    .verticalScroll(rememberScrollState()),
            ) { screenContent() }
        }

        snackbarMessage?.let { msg ->
            Box(Modifier.align(Alignment.BottomCenter).padding(Spacing.lg)) {
                DismissibleSnackBar(visibility = true) { Text(msg) }
            }
        }
    }

    if (showDisableDialog) {
        AlertDialog(
            onDismissRequest = { showDisableDialog = false },
            title = { Text(stringResource(Res.string.disable_2fa_confirmation)) },
            text = { Text(stringResource(Res.string.disable_2fa_warning)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDisableDialog = false
                        scope.launch {
                            isLoading = true
                            try {
                                disableTwoFactor(client, accessToken)
                                isEnabled = false
                            } catch (e: Exception) {
                                snackbarMessage = e.message
                            } finally { isLoading = false }
                        }
                    }
                ) { Text(stringResource(Res.string.yes_disable_2fa)) }
            },
            dismissButton = {
                TextButton(onClick = { showDisableDialog = false }) {
                    Text(stringResource(Res.string.keep_account_safe))
                }
            }
        )
    }
}
