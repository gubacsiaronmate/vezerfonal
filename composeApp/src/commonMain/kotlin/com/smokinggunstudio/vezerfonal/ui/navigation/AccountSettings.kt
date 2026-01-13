package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.LocalTokenStorage
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.ui.screens.AccountSettingsScreen

data class AccountSettings(
    val token: String,
    val userStr: String,
) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val tokenStorage = LocalTokenStorage.current
        val navigator = LocalNavigator.currentOrThrow
        
        AccountSettingsScreen(
            user = userStr.toDTO<UserData>(),
            client = client,
            accessToken = token,
            tokenStorage = tokenStorage,
            onLogOutClick = { navigator.replaceAll(Landing) },
            onChangePasswordClick = { navigator.push(ChangePassword) }
        )
    }
}