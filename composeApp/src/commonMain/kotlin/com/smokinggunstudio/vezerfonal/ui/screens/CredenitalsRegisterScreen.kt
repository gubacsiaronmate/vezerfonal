package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.EmailField
import com.smokinggunstudio.vezerfonal.ui.components.PasswordField
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.helpers.isValidEmail
import com.smokinggunstudio.vezerfonal.ui.state.controller.RegisterStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable fun CredentialsRegisterScreen(
    snapshot: RegisterStateModel,
    onClick: CallbackFunction<RegisterStateModel>
) {
    val state = remember { RegisterStateController(snapshot) }
    val confirmPassword = remember { mutableStateOf("") }
    val windowSizeInfo = LocalWindowSizeInfo.current
    val isWide = windowSizeInfo.widthClass != WindowWidthClass.Compact

    @Composable
    fun FormContent() {
        RegisterText()

        Spacer(Modifier.height(Spacing.xl))

        EmailField(
            value = state.email,
            labelText = stringResource(Res.string.email_address),
            onValueChanged = state::updateEmail
        )

        PasswordField(
            value = state.password,
            labelText = stringResource(Res.string.password),
            onValueChanged = state::updatePassword,
            supportingText = stringResource(Res.string.password_must_be_at_least_8_characters_long)
        )

        PasswordField(
            value = confirmPassword.value,
            labelText = stringResource(Res.string.confirm_password),
            onValueChanged = { confirmPassword.value = it },
            supportingText = stringResource(Res.string.passwords_must_match)
        )

        Spacer(Modifier.height(Spacing.sm))

        AnimatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick(state.snapshot()) },
            enabled = (
                state.password == confirmPassword.value
                && state.password.length >= 8
                && state.email.isNotBlank()
                && state.email.isValidEmail()
                && confirmPassword.value.isNotBlank()
            ),
        ) { Text(stringResource(Res.string.proceed)) }
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
                    modifier = Modifier.padding(Spacing.xl),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Spacer(Modifier.height(Spacing.sm))
                    FormContent()
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.lg)
                    .padding(top = Spacing.xxxl),
            ) {
                FormContent()
            }
        }
    }
}
