package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.LocalTokenStorage
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.screens.AccountSettingsScreen
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

data class AccountSettings(
    val token: String,
    val user: UserData,
) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val tokenStorage = LocalTokenStorage.current
        val navigator = LocalNavigator.currentOrThrow
        
        AccountSettingsScreen(
            user = user,
            client = client,
            accessToken = token,
            tokenStorage = tokenStorage,
            onLogOutClick = { navigator.replaceAll(Landing()) },
            onChangePasswordClick = { navigator.push(ChangePassword) }
        )
    }
}