package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.navigator.Navigator
import java.awt.KeyboardFocusManager
import java.awt.KeyEventDispatcher
import java.awt.event.KeyEvent
import kotlin.system.exitProcess

actual object BackHandler {
    @Composable
    actual fun Bind(navigator: Navigator) {
        val canGoBack = !navigator.isEmpty
        DisposableEffect(Unit) {
            val dispatcher = KeyEventDispatcher { e ->
                if (e.id == KeyEvent.KEY_PRESSED) {
                    val isBackKey = when (e.keyCode) {
                        KeyEvent.VK_ESCAPE -> true
                        KeyEvent.VK_BACK_SPACE -> !(e.isControlDown || e.isMetaDown)
                        // Alt + Left Arrow is a common back gesture on desktop
                        KeyEvent.VK_LEFT -> e.isAltDown
                        else -> false
                    }
                    if (isBackKey) {
                        if (canGoBack) navigator.pop() else exitProcess(0)
                        true
                    } else false
                } else false
            }
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher)
            onDispose {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher)
            }
        }
    }
}