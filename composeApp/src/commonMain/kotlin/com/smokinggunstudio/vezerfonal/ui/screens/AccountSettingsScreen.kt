package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.logOutRequest
import com.smokinggunstudio.vezerfonal.ui.components.AccountSettingsNameCard
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.SettingRow
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun AccountSettingsScreen(
    user: UserData,
    client: HttpClient,
    accessToken: String,
    tokenStorage: TokenStorage,
    onLogOutClick: Function,
    onChangePasswordClick: Function,
) {
    val scope = rememberCoroutineScope()
    var error by remember { mutableStateOf<Throwable?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column {
            AccountSettingsNameCard(user)
            SettingRow(
                imageVector = Icons.Default.Password,
                text = stringResource(Res.string.change_password),
                onClick = onChangePasswordClick
            )
            SettingRow(
                imageVector = Icons.Outlined.Shield,
                text = stringResource(Res.string.set_up_2fa)
            )
            SettingRow(
                imageVector = Icons.Outlined.DeleteForever,
                text = stringResource(Res.string.request_account_deletion)
            )
            SettingRow(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                text = stringResource(Res.string.log_out)
            ) {
                try {
                    scope.launch {
                        logOutRequest(accessToken, client)
                        tokenStorage.clearTokens()
                        onLogOutClick()
                    }
                } catch (e: UnauthorizedException) {
                    error = e
                }
            }
        }

        if (error != null) ErrorDialog(error!!.message!!, true)
    }
}