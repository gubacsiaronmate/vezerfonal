package com.smokinggunstudio.vezerfonal.ui.helpers

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.*

actual fun String.svgXMLToByteArray(size: Int, quality: Int): ByteArray {
    TODO("Not yet implemented")
}

@OptIn(BetaInteropApi::class)
actual fun String.toAscii(): String =
    (NSString.create(this))
        .stringByFoldingWithOptions(
            NSDiacriticInsensitiveSearch,
            NSLocale.currentLocale
        )
        .replace(Regex("[^\\x00-\\x7F]"), "")