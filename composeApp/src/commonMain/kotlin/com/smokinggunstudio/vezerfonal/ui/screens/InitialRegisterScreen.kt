package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.OrOptionDivider
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.state.controller.RegisterStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create_organization
import vezerfonal.composeapp.generated.resources.proceed
import vezerfonal.composeapp.generated.resources.registration_code

@Composable fun InitialRegisterScreen(
    onCreateOrgClick: Function,
    onContinueClick: CallbackFunction<RegisterStateModel>,
) {
    val nonAdminRegisterState = remember { RegisterStateController(RegisterStateModel()) }
    val windowSizeInfo = LocalWindowSizeInfo.current
    val isWide = windowSizeInfo.widthClass != WindowWidthClass.Compact

    @Composable
    fun FormContent() {
        RegisterText()

        Spacer(Modifier.height(Spacing.xl))

        OutlinedTextField(
            value = nonAdminRegisterState.extra,
            onValueChange = nonAdminRegisterState::updateExtra,
            label = { Text(stringResource(Res.string.registration_code), color = MaterialTheme.colorScheme.onSurface) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Spacing.xl))

        AnimatedButton(
            onClick = {
                onContinueClick(
                    nonAdminRegisterState
                        .apply { setIsAdmin(false) }
                        .snapshot()
                )
            },
            enabled = nonAdminRegisterState.extra.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.proceed),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(Modifier.height(Spacing.md))

        OrOptionDivider()

        Spacer(Modifier.height(Spacing.md))

        AnimatedButton(
            onClick = { onCreateOrgClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.create_organization),
                color = MaterialTheme.colorScheme.onPrimary
            )
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
