package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.ui.graphics.ImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData

actual fun FileData.svgXMLToByteArray(size: Int, quality: Int): ImageBitmap {
    TODO("Not yet implemented")
}

actual fun String.toAscii(): String =
    (js("this.normalize('NFD')") as String)
        .replace(Regex("\\p{M}+"), "")
        .replace(Regex("[^\\x00-\\x7F]"), "")