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
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.registerBasic
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.state.controller.RegisterStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable fun ProfileCreationScreen(
    snapshot: RegisterStateModel,
    onClick: CallbackFunction<TokenResponse>
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    var rememberMe by remember { mutableStateOf(false) }
    val state = remember { RegisterStateController(snapshot) }
    var areTermsAccepted by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    val windowSizeInfo = LocalWindowSizeInfo.current
    val isWide = windowSizeInfo.widthClass != WindowWidthClass.Compact

    @Composable
    fun FormContent() {
        RegisterText()

        Spacer(Modifier.height(Spacing.xl))

        OutlinedTextField(
            value = state.identifier,
            onValueChange = state::updateIdentifier,
            label = {
                Text(
                    stringResource(Res.string.identifier),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Spacing.sm))

        OutlinedTextField(
            value = state.name,
            onValueChange = { state.updateName(it) },
            label = {
                Text(
                    stringResource(Res.string.display_name),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Spacing.sm))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = areTermsAccepted,
                onCheckedChange = { areTermsAccepted = it }
            )
            Text(
                text = stringResource(Res.string.accept_terms),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it }
            )
            Text(
                text = stringResource(Res.string.remember_me),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.height(Spacing.sm))

        AnimatedButton(
            enabled = (
                state.identifier.isNotBlank()
                && state.name.isNotBlank()
                && areTermsAccepted
            ),
            onClick = {
                scope.launch {
                    try {
                        val tokens = registerBasic(
                            user = state.toUserData(),
                            rememberMe = rememberMe,
                            client = client
                        )
                        onClick(tokens)
                    } catch (e: UnauthorizedException) {
                        error = e
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text(stringResource(Res.string.create_account)) }
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

        if (error != null) ErrorDialog(error!!, Modifier.align(Alignment.Center))
    }
}
