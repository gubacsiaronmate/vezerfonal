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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.ui.helpers.BackHandler
import com.smokinggunstudio.vezerfonal.ui.navigation.Landing
import com.smokinggunstudio.vezerfonal.ui.theme.VezerfonalTheme

@Composable fun App() {
    var darkModeState by remember { mutableStateOf<Boolean?>(null) }
    
    VezerfonalTheme(darkModeState ?: isSystemInDarkTheme()) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Navigator(Landing(darkModeState) { darkModeState = it })
            BackHandler.Bind(LocalNavigator.currentOrThrow)
        }
    }
}