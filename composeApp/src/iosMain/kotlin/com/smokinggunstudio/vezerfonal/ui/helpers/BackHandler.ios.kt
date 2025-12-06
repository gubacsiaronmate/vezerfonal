package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.navigator.Navigator
import platform.posix.exit

actual object BackHandler {
    @Composable
    actual fun Bind(navigator: Navigator) {
        val canGoBack = !navigator.isEmpty
        // Register a bridge callback that Swift can trigger (via edge swipe or button)
        DisposableEffect(canGoBack, navigator) {
            iosSetBackHandler {
                if (canGoBack) navigator.pop() else exit(0)
            }
            onDispose {
                iosSetBackHandler(null)
            }
        }
    }
}