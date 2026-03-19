package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.logOutRequest
import com.smokinggunstudio.vezerfonal.network.api.updateDisplayName
import com.smokinggunstudio.vezerfonal.ui.components.AccountSettingsNameCard
import com.smokinggunstudio.vezerfonal.ui.helpers.HomeCache
import com.smokinggunstudio.vezerfonal.ui.components.SettingRow
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun AccountSettingsScreen(
    user: UserData,
    accessToken: String,
    tokenStorage: TokenStorage,
    onLogOutClick: Function,
    onChangePasswordClick: Function,
    onRequestAccountDeletionClick: Function,
    onTwoFactorClick: Function,
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    val isExpanded = LocalWindowSizeInfo.current.widthClass == WindowWidthClass.Expanded
    var displayName by remember { mutableStateOf(user.name) }
    var showEditNameDialog by remember { mutableStateOf(false) }
    var editNameInput by remember { mutableStateOf("") }
    var editNameError by remember { mutableStateOf<Throwable?>(null) }

    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text(stringResource(Res.string.edit_display_name)) },
            text = {
                OutlinedTextField(
                    value = editNameInput,
                    onValueChange = { editNameInput = it },
                    label = { Text(stringResource(Res.string.display_name)) },
                    singleLine = true,
                )
            },
            confirmButton = {
                TextButton(
                    enabled = editNameInput.isNotBlank(),
                    onClick = {
                        val name = editNameInput.trim()
                        scope.launch {
                            try {
                                updateDisplayName(client, accessToken, name)
                                displayName = name
                                showEditNameDialog = false
                            } catch (e: Exception) {
                                editNameError = e
                            }
                        }
                    },
                ) { Text(stringResource(Res.string.applyStr)) }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text(stringResource(Res.string.cancel))
                }
            },
        )
    }

    if (editNameError != null) {
        AlertDialog(
            onDismissRequest = { editNameError = null },
            title = { Text(stringResource(Res.string.error_happened)) },
            text = { Text(editNameError!!.message ?: "") },
            confirmButton = {
                TextButton(onClick = { editNameError = null }) {
                    Text(stringResource(Res.string.close))
                }
            },
        )
    }

    val screenContent: @Composable ColumnScope.() -> Unit = {
        AccountSettingsNameCard(
            user = user,
            displayName = displayName,
            onEditClick = {
                editNameInput = displayName
                showEditNameDialog = true
            },
        )
        SettingRow(
            imageVector = Icons.Default.Password,
            text = stringResource(Res.string.change_password),
            onClick = onChangePasswordClick,
        )
        SettingRow(
            imageVector = Icons.Outlined.Shield,
            text = stringResource(Res.string.set_up_2fa),
            onClick = onTwoFactorClick,
        )
        SettingRow(
            imageVector = Icons.Outlined.DeleteForever,
            text = stringResource(Res.string.request_account_deletion),
            onClick = onRequestAccountDeletionClick,
        )
        SettingRow(
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            text = stringResource(Res.string.log_out),
        ) {
            scope.launch {
                try {
                    logOutRequest(accessToken, client)
                } catch (_: Exception) {
                    // Best-effort server-side logout. If the account was deleted
                    // or any other error occurs, proceed with local logout anyway.
                }
                tokenStorage.clearTokens()
                HomeCache.invalidate()
                onLogOutClick()
            }
        }

        Spacer(Modifier.height(Spacing.xl))
    }

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
                        .verticalScroll(rememberScrollState()),
                ) { screenContent() }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) { screenContent() }
    }
}
