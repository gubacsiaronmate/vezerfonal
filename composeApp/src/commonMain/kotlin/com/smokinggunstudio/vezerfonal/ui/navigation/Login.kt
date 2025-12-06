package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.LocalTokenStorage
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.screens.LoginScreen
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

data class Login(
    val orgs: List<OrgData>,
) : Screen {
    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val client = LocalHttpClient.current
        val tokenStorage = LocalTokenStorage.current
        val navigator = LocalNavigator.currentOrThrow
        
        LoginScreen(client, orgs) { newTokens ->
            navigator.push(Home(newTokens.accessToken))
            scope.launch { tokenStorage.saveTokens(newTokens) }
        }
    }
}