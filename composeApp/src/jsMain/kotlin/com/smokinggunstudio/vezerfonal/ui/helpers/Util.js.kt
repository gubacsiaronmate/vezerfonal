package com.smokinggunstudio.vezerfonal.ui.helpers

actual fun String.svgXMLToByteArray(size: Int, quality: Int): ByteArray {
    TODO("Not yet implemented")
}

actual fun String.toAscii(): String =
    (js("this.normalize('NFD')") as String)
        .replace(Regex("\\p{M}+"), "")
        .replace(Regex("[^\\x00-\\x7F]"), "")