package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.navigator.Navigator
import androidx.activity.compose.BackHandler as AndroidXBackHandler
import com.smokinggunstudio.vezerfonal.helpers.CurrentActivityProvider

actual object BackHandler {
    @Composable
    actual fun Bind(navigator: Navigator) {
        
        val canGoBack = !navigator.isEmpty
        AndroidXBackHandler(enabled = true) {
            if (canGoBack) navigator.pop()
            else CurrentActivityProvider.current?.finish()
        }
    }
}