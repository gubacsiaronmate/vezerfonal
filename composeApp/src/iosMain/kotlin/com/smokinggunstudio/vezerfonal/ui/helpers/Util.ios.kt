package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.ui.graphics.ImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData
import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.*

actual fun FileData.svgXMLToByteArray(size: Int, quality: Int): ImageBitmap {
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