package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.activity.compose.BackHandler as AndroidXBackHandler
import com.smokinggunstudio.vezerfonal.helpers.CurrentActivityProvider
import moe.tlaster.precompose.navigation.Navigator

actual object BackHandler {
    @Composable
    actual fun Bind(navigator: Navigator) {
        val canGoBack = navigator.canGoBack.collectAsState(initial = false).value
        AndroidXBackHandler(enabled = true) {
            if (canGoBack) navigator.goBack()
            else CurrentActivityProvider.current?.finish()
        }
    }
}