package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.screens.CreateOrganizationScreen
import com.smokinggunstudio.vezerfonal.ui.state.AdminRegisterState
import io.ktor.client.HttpClient

data class CreateOrg(val registerState: AdminRegisterState) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val navigator = LocalNavigator.currentOrThrow
        
        CreateOrganizationScreen(registerState, client) {
            navigator.push(Register(2, registerState))
        }
    }
}