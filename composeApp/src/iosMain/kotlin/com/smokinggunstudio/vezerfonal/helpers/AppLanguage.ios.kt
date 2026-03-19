package com.smokinggunstudio.vezerfonal.helpers

import platform.Foundation.NSUserDefaults

actual fun applyLanguage(tag: String) {
    NSUserDefaults.standardUserDefaults.setObject(listOf(tag), forKey = "AppleLanguages")
    NSUserDefaults.standardUserDefaults.synchronize()
}
