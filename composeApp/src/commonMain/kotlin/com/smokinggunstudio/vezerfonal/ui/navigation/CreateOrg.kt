package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.screens.CreateOrganizationScreen
import com.smokinggunstudio.vezerfonal.ui.state.AdminRegisterState
import io.ktor.client.HttpClient

class CreateOrg(
    val client: HttpClient,
    val isDarkMode: Boolean?,
    val tokenStorage: TokenStorage,
    val registerState: AdminRegisterState,
    val darkModeStateCallback: CallbackEvent<Boolean>
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        CreateOrganizationScreen(registerState, client) {
            navigator.push(
                Register(
                    page = 2,
                    client = client,
                    isDarkMode = isDarkMode,
                    regState = registerState,
                    tokenStorage = tokenStorage,
                    darkModeStateCallback = darkModeStateCallback
                )
            )
        }
    }
}