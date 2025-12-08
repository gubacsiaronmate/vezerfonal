package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.LocalTokenStorage
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.network.api.getAllOrgsRequest
import com.smokinggunstudio.vezerfonal.network.helpers.getAccessToken
import com.smokinggunstudio.vezerfonal.ui.screens.LandingPageScreen

data object Landing : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val tokenStorage = LocalTokenStorage.current
        val navigator = LocalNavigator.currentOrThrow
        var token: String? by remember { mutableStateOf(null) }
        var loaded by remember { mutableStateOf(false) }
        var orgs by remember { mutableStateOf<List<OrgData>>(emptyList()) }
        
        LaunchedEffect(Unit) {
            val t = getAccessToken(tokenStorage, client)
            val o = getAllOrgsRequest(client)
            token = t
            orgs = o
            loaded = true
        }
        
        if (!loaded) {
            Box(Modifier.fillMaxSize()) { LinearProgressIndicator(Modifier.align(Alignment.Center)) }
            return
        }
        
        if (token != null) {
            LaunchedEffect(token) { navigator.replaceAll(Home(token!!)) }
            return
        }
        
        LandingPageScreen(
            onRegisterClick = { navigator.push(Register(1, null)) },
            onLoginClick = { navigator.push(Login(orgs)) },
        )
    }
}