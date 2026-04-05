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
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.helpers.NotCreatedException
import com.smokinggunstudio.vezerfonal.helpers.getExtId
import com.smokinggunstudio.vezerfonal.network.api.createOrgRequest
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.state.controller.RegisterStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create
import vezerfonal.composeapp.generated.resources.create_organization
import vezerfonal.composeapp.generated.resources.organization_name

@Composable
fun CreateOrganizationScreen(
    snapshot: RegisterStateModel,
    onClick: CallbackFunction<RegisterStateModel>
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    val state = remember { RegisterStateController(snapshot) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    val windowSizeInfo = LocalWindowSizeInfo.current
    val isWide = windowSizeInfo.widthClass != WindowWidthClass.Compact

    @Composable
    fun FormContent() {
        Text(
            text = stringResource(Res.string.create_organization),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(Modifier.height(Spacing.xl))

        OutlinedTextField(
            value = state.extra,
            onValueChange = state::updateExtra,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = {
                Text(
                    text = stringResource(Res.string.organization_name),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        )

        Spacer(Modifier.height(Spacing.md))

        AnimatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                try {
                    scope.launch {
                        if (createOrgRequest(
                            org = OrgData(
                                name = state.extra,
                                externalId = getExtId()
                            ),
                            client = client
                        )) {
                            state.setIsAdmin(true)
                            val snapshot = state.snapshot()
                            onClick(snapshot)
                        }
                    }
                } catch (e: NotCreatedException) { error = e }
            }
        ) { Text(text = stringResource(Res.string.create)) }
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
