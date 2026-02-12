package com.smokinggunstudio.vezerfonal.ui.helpers

import java.text.Normalizer

actual fun String.svgXMLToByteArray(size: Int, quality: Int): ByteArray {
    TODO("Not yet implemented")
}

actual fun String.toAscii(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .replace(Regex("[^\\x00-\\x7F]"), "")