package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.getAllOrgsRequest
import com.smokinggunstudio.vezerfonal.network.client.createHttpClient
import com.smokinggunstudio.vezerfonal.network.helpers.getAccessToken
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.screens.LandingPageScreen
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState

class Landing(
    val isDarkMode: Boolean?,
    val darkModeStateCallback: CallbackEvent<Boolean>
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val client = createHttpClient()
        val tokenStorage = remember { TokenStorage() }
        var token: String? by remember { mutableStateOf(null) }
        var loaded by remember { mutableStateOf(false) }
        lateinit var orgs: List<OrgData>
        
        LaunchedEffect(Unit) {
            val t = getAccessToken(tokenStorage, client)
            val o = getAllOrgsRequest(client)
            token = t
            orgs = o
            loaded = true
        }
        
        if (!loaded) return
        
        if (token != null) navigator.replaceAll(
            Home(
                accessToken = token!!,
                client = client,
                isDarkMode = isDarkMode,
                tokenStorage = tokenStorage,
                darkModeStateSwitch = darkModeStateCallback
            )
        )
        
        LandingPageScreen(
            onRegisterClick = {
                navigator.push(
                    Register(
                        page = 1,
                        client = client,
                        isDarkMode = isDarkMode,
                        regState = null,
                        tokenStorage = tokenStorage,
                        darkModeStateCallback = darkModeStateCallback
                    )
                )
            },
            onLoginClick = {
                navigator.push(
                    Login(
                        client = client,
                        orgs = orgs,
                        tokenStorage = tokenStorage,
                        isDarkMode = isDarkMode,
                        darkModeStateCallback = darkModeStateCallback
                    )
                )
            },
        )
    }
}