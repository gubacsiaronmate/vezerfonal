package com.smokinggunstudio.vezerfonal.ui.helpers

/**
 * Simple bridge to allow Swift/iOS code to trigger a back action inside Compose.
 * Exposed as top-level functions so Swift can call: IosBackBridgeKt.iosTriggerBack()
 */
private var iosBackCallback: (() -> Unit)? = null

fun iosSetBackHandler(callback: (() -> Unit)?) {
    iosBackCallback = callback
}

fun iosTriggerBack() {
    iosBackCallback?.invoke()
}
