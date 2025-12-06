package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.screens.LoginScreen
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

class Login(
    val client: HttpClient,
    val orgs: List<OrgData>,
    val tokenStorage: TokenStorage,
    val isDarkMode: Boolean?,
    val darkModeStateCallback: CallbackEvent<Boolean>
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        
        LoginScreen(client, orgs) { newTokens ->
            navigator.push(
                Home(
                    accessToken = newTokens.accessToken,
                    client = client,
                    isDarkMode = isDarkMode,
                    tokenStorage = tokenStorage,
                    darkModeStateSwitch = darkModeStateCallback
                )
            )
            scope.launch { tokenStorage.saveTokens(newTokens) }
        }
    }
}