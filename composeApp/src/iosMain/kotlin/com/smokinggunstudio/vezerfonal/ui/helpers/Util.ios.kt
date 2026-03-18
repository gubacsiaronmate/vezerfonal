package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData
import kotlinx.cinterop.BetaInteropApi
import org.jetbrains.skia.Data
import org.jetbrains.skia.Surface
import org.jetbrains.skia.svg.SVGDOM
import platform.Foundation.*

actual fun FileData.svgXMLToByteArray(size: Int, quality: Int): ImageBitmap {
    val svgDom = SVGDOM(Data.makeFromBytes(bytes))
    val surface = Surface.makeRasterN32Premul(size, size)
    svgDom.setContainerSize(size.toFloat(), size.toFloat())
    svgDom.render(surface.canvas)
    return surface.makeImageSnapshot().toComposeImageBitmap()
}

@OptIn(BetaInteropApi::class)
actual fun String.toAscii(): String =
    (NSString.create(this))
        .stringByFoldingWithOptions(
            NSDiacriticInsensitiveSearch,
            NSLocale.currentLocale
        )
        .replace(Regex("[^\\x00-\\x7F]"), "")
