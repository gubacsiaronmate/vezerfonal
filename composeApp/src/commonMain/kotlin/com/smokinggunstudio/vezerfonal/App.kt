package com.smokinggunstudio.vezerfonal

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.client.createHttpClient
import com.smokinggunstudio.vezerfonal.ui.helpers.BackHandler
import com.smokinggunstudio.vezerfonal.ui.navigation.Landing
import com.smokinggunstudio.vezerfonal.ui.theme.VezerfonalTheme
import io.ktor.client.HttpClient

val LocalHttpClient = staticCompositionLocalOf<HttpClient> { error("No HttpClient provided") }
val LocalTokenStorage = staticCompositionLocalOf<TokenStorage> { error("No TokenStorage provided") }
val LocalDarkModeState = staticCompositionLocalOf<MutableState<Boolean?>> { error("No dark mode state provided.") }

@Composable fun App() {
    val darkModeState = remember { mutableStateOf<Boolean?>(null) }
    val tokenStorage = remember { TokenStorage() }
    val client = remember { createHttpClient() }
    
    VezerfonalTheme(darkModeState.value ?: isSystemInDarkTheme()) {
        
        CompositionLocalProvider(
            LocalHttpClient provides client,
            LocalTokenStorage provides tokenStorage,
            LocalDarkModeState provides darkModeState,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                Navigator(Landing) {
                    CurrentScreen()
                    BackHandler.Bind(it)
                }
            }
        }
    }
}