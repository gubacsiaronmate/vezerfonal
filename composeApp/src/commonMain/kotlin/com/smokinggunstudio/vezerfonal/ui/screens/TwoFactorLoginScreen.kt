package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.TwoFactorLoginRequest
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.network.api.twoFactorLogin
import com.smokinggunstudio.vezerfonal.ui.components.DismissibleSnackBar
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun TwoFactorLoginScreen(
    email: String,
    orgExternalId: String,
    rememberMe: Boolean,
    client: HttpClient,
    onSuccess: CallbackFunction<TokenResponse>,
) {
    var code by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val codeAsInt = code.toIntOrNull() ?: 0
    val canSubmit = !isLoading && code.length == 6 && codeAsInt > 0
    val isWide = LocalWindowSizeInfo.current.widthClass != WindowWidthClass.Compact

    @Composable
    fun FormContent() {
        Icon(
            imageVector = Icons.Outlined.Shield,
            contentDescription = null,
            modifier = Modifier.size(48.dp).fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(Spacing.md))
        Text(
            text = stringResource(Res.string.two_factor_authentication),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(Spacing.sm))
        Text(
            text = stringResource(Res.string.enter_code),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(Spacing.xl))
        OutlinedTextField(
            value = code,
            onValueChange = { if (it.length <= 6) code = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(Res.string.code)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(Spacing.lg))
        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    try {
                        val tokens = twoFactorLogin(
                            client,
                            TwoFactorLoginRequest(
                                email = email,
                                orgExternalId = orgExternalId,
                                code = codeAsInt,
                                rememberMe = rememberMe,
                            )
                        )
                        onSuccess(tokens)
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
                    modifier = Modifier.size(Spacing.lg),
                    strokeWidth = Spacing.xs,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            else
                Text(stringResource(Res.string.proceed))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        if (isWide) {
            Card(
                modifier = Modifier.widthIn(max = 480.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.xl),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Spacer(Modifier.height(Spacing.sm))
                    FormContent()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg)
                    .padding(top = Spacing.xxxl),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FormContent()
            }
        }

        snackbarMessage?.let { msg ->
            Box(Modifier.align(Alignment.BottomCenter).padding(Spacing.lg)) {
                DismissibleSnackBar(visibility = true) { Text(msg) }
            }
        }
    }
}
