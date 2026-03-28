package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.ui.screens.RequestAccountDeletionScreen

data class RequestAccountDeletion(val token: String) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val navigator = LocalNavigator.currentOrThrow

        RequestAccountDeletionScreen(
            accessToken = token,
            client = client,
            onCancel = { navigator.pop() },
            onSuccess = { navigator.replaceAll(Landing) },
        )
    }
}
