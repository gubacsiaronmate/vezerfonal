package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.screens.AccountSettingsScreen
import io.ktor.client.HttpClient

class AccountSettings(
    val token: String,
    val user: UserData,
    val client: HttpClient,
    val isDarkMode: Boolean?,
    val tokenStorage: TokenStorage,
    val darkModeStateCallback: CallbackEvent<Boolean>
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        AccountSettingsScreen(
            user = user,
            client = client,
            accessToken = token,
            tokenStorage = tokenStorage,
            onLogOutClick = { navigator.replaceAll(Landing(isDarkMode, darkModeStateCallback)) },
            onChangePasswordClick = { navigator.push(ChangePassword) }
        )
    }
}