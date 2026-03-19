package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.browser.window

actual fun applyLanguage(tag: String) {
    window.location.reload()
}
