@file:OptIn(ExperimentalWasmJsInterop::class)

package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.browser.window
import org.w3c.dom.events.Event
import kotlin.js.ExperimentalWasmJsInterop
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.KeyboardEvent

// In wasmJs, EventListener is external — you can neither SAM-construct it nor extend it with a
// non-external type. This helper wraps a Kotlin lambda into a JS object { handleEvent: fn }
// which satisfies the EventListener interface contract. The returned reference is stable so
// removeEventListener can match it by identity.
@JsFun("(fn) => ({ handleEvent: (e) => fn(e) })")
private external fun jsEventListener(handler: (Event) -> Unit): EventListener

actual object BackHandler {
    @Composable
    actual fun Bind(navigator: Navigator) {
        val canGoBack = !navigator.isEmpty
        val canGoBackState = rememberUpdatedState(canGoBack)

        DisposableEffect(Unit) {
            val onPopState = jsEventListener { _ ->
                if (canGoBackState.value) {
                    navigator.pop()
                    window.history.pushState(null, "", window.location.href)
                } else {
                    if (window.history.length <= 1) window.close()
                }
            }
            val onKeyDown = jsEventListener { e ->
                val ke = e as? KeyboardEvent
                if (ke != null) {
                    val backKey = ke.altKey && ke.key == "ArrowLeft"
                    if (backKey) {
                        ke.preventDefault()
                        if (canGoBackState.value) navigator.pop() else window.close()
                    }
                }
            }

            window.addEventListener("popstate", onPopState)
            window.addEventListener("keydown", onKeyDown)
            window.history.pushState(null, "", window.location.href)

            onDispose {
                window.removeEventListener("popstate", onPopState)
                window.removeEventListener("keydown", onKeyDown)
            }
        }
    }
}
