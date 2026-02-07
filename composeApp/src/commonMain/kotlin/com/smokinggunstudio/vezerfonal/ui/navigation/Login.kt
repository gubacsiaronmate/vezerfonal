package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.LocalTokenStorage
import com.smokinggunstudio.vezerfonal.ui.screens.LoginScreen
import kotlinx.coroutines.launch

data class Login(
    val orgsStr: List<String>,
) : Screen {
    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val tokenStorage = LocalTokenStorage.current
        val navigator = LocalNavigator.currentOrThrow
        
        LoginScreen(orgsStr) { newTokens ->
            navigator.push(Home(newTokens.accessToken))
            scope.launch { tokenStorage.saveTokens(newTokens) }
        }
    }
}