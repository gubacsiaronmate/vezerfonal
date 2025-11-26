package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.browser.window
import moe.tlaster.precompose.navigation.Navigator
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

actual object BackHandler {
    @Composable
    actual fun Bind(navigator: Navigator) {
        val canGoBack = navigator.canGoBack.collectAsState(initial = false).value
        val canGoBackState = rememberUpdatedState(canGoBack)

        DisposableEffect(Unit) {
            val onPopState: (Event) -> Unit = {
                if (canGoBackState.value) {
                    navigator.goBack()
                    // Re-push a state to keep the user on the SPA and handle future backs
                    window.history.pushState(null, "", window.location.href)
                } else {
                    // No more in-app back stack; allow navigating away or attempt to close
                    if (window.history.length <= 1) {
                        // This will usually be blocked by the browser, but we try
                        window.close()
                    }
                }
            }
            val onKeyDown: (org.w3c.dom.events.Event) -> Unit = { e ->
                val ke = e as? KeyboardEvent
                if (ke != null) {
                    val backKey = (ke.key == "Backspace" && !ke.ctrlKey && !ke.metaKey) ||
                            (ke.altKey && ke.key == "ArrowLeft")
                    if (backKey) {
                        ke.preventDefault()
                        if (canGoBackState.value) navigator.goBack() else window.close()
                    }
                }
            }

            window.addEventListener("popstate", onPopState)
            window.addEventListener("keydown", onKeyDown)
            // Ensure there's always one extra history entry so native back triggers popstate first
            window.history.pushState(null, "", window.location.href)

            onDispose {
                window.removeEventListener("popstate", onPopState)
                window.removeEventListener("keydown", onKeyDown)
            }
        }
    }
}