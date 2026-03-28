package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.LocalTokenStorage
import com.smokinggunstudio.vezerfonal.ui.screens.TwoFactorLoginScreen
import kotlinx.coroutines.launch

data class TwoFactorLogin(
    val email: String,
    val orgExternalId: String,
    val rememberMe: Boolean,
) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val tokenStorage = LocalTokenStorage.current
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()

        TwoFactorLoginScreen(
            email = email,
            orgExternalId = orgExternalId,
            rememberMe = rememberMe,
            client = client,
        ) { tokens ->
            navigator.push(Home(tokens.accessToken))
            scope.launch { tokenStorage.saveTokens(tokens) }
        }
    }
}
