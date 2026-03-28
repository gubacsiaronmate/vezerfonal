package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.ui.screens.TwoFactorSetupScreen

data class TwoFactorSetup(
    val token: String,
    val twoFactorEnabled: Boolean,
) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val navigator = LocalNavigator.currentOrThrow

        TwoFactorSetupScreen(
            twoFactorEnabled = twoFactorEnabled,
            accessToken = token,
            client = client,
            onBack = { navigator.pop() },
        )
    }
}
