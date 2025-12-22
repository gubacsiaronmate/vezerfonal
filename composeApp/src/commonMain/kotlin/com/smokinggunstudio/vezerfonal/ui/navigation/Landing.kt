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
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.network.api.getAllOrgsRequest
import com.smokinggunstudio.vezerfonal.network.api.getAccessToken
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.screens.LandingPageScreen

data object Landing: Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val tokenStorage = LocalTokenStorage.current
        val navigator = LocalNavigator.currentOrThrow
        var token: String? by remember { mutableStateOf(null) }
        var loaded by remember { mutableStateOf(false) }
        var orgs by remember { mutableStateOf<List<OrgData>>(emptyList()) }
        var error by remember { mutableStateOf<Throwable?>(null)}
        
        LaunchedEffect(Unit) {
            val o = try {
                getAllOrgsRequest(client)
            } catch (e: Exception) {
                error = e
                return@LaunchedEffect
            }
            orgs = o
            val t = try {
                getAccessToken(tokenStorage, client)
            } catch (_: UnauthorizedException) {
                loaded = true
                return@LaunchedEffect
            }
            token = t
            loaded = true
        }
        
        Box(Modifier.fillMaxSize()) {
            if (!loaded) {
                Box(Modifier.fillMaxSize()) {
                    LinearProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
            
            if (token != null) {
                LaunchedEffect(token) { navigator.replaceAll(HomePage(token!!)) }
                return
            }
            
            LandingPageScreen(
                onRegisterClick = { navigator.push(Register(1, null)) },
                onLoginClick = { navigator.push(Login(orgs.map { it.toSerialized() })) },
            )
            
            if (error != null) {
                ErrorDialog(error!!.message!!, false)
                return
            }
        }
    }
}