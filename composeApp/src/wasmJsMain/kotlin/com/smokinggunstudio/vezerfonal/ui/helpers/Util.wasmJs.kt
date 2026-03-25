@file:OptIn(ExperimentalWasmJsInterop::class)

package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData
import kotlin.js.ExperimentalWasmJsInterop
import org.jetbrains.skia.Data
import org.jetbrains.skia.Surface
import org.jetbrains.skia.svg.SVGDOM

actual fun FileData.svgXMLToByteArray(size: Int, quality: Int): ImageBitmap {
    val svgDom = SVGDOM(Data.makeFromBytes(bytes))
    val surface = Surface.makeRasterN32Premul(size, size)
    svgDom.setContainerSize(size.toFloat(), size.toFloat())
    svgDom.render(surface.canvas)
    return surface.makeImageSnapshot().toComposeImageBitmap()
}

// In wasmJs, String is not a JS string — use @JsFun to call JS normalize on the JS side.
@JsFun("(s) => s.normalize('NFD').replace(/[\\u0300-\\u036f]/g, '').replace(/[^\\x00-\\x7F]/g, '')")
private external fun normalizeToAscii(s: String): String

actual fun String.toAscii(): String = normalizeToAscii(this)
