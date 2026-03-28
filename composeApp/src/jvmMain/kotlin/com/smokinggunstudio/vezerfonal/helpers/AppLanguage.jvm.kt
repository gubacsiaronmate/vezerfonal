package com.smokinggunstudio.vezerfonal.helpers

import java.util.Locale

actual fun applyLanguage(tag: String) {
    Locale.setDefault(Locale.forLanguageTag(tag))
}
