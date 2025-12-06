package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

/**
 * Cross‑platform BackHandler facade.
 *
 * Behavior:
 * 1) If the navigator can go back, it pops one entry.
 * 2) If it cannot go back, it closes the app/window on the current platform.
 */
expect object BackHandler {
    @Composable
    fun Bind(navigator: Navigator)
}