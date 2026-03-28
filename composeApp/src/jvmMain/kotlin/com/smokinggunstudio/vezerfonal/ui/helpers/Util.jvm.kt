package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData
import java.text.Normalizer
import org.jetbrains.skia.Data
import org.jetbrains.skia.Surface
import org.jetbrains.skia.svg.SVGDOM

actual fun FileData.svgXMLToByteArray(size: Int, quality: Int): ImageBitmap {
    if (bytes.isEmpty()) {
        System.err.println("[svgXMLToByteArray] empty bytes — returning blank bitmap")
        return Surface.makeRasterN32Premul(size, size).makeImageSnapshot().toComposeImageBitmap()
    }
    return try {
        val svgDom = SVGDOM(Data.makeFromBytes(bytes))
        val surface = Surface.makeRasterN32Premul(size, size)
        svgDom.setContainerSize(size.toFloat(), size.toFloat())
        svgDom.render(surface.canvas)
        surface.makeImageSnapshot().toComposeImageBitmap()
    } catch (e: RuntimeException) {
        System.err.println("[svgXMLToByteArray] SVGDOM failed: ${e.message}")
        System.err.println("[svgXMLToByteArray] content preview: ${bytes.decodeToString().take(300)}")
        Surface.makeRasterN32Premul(size, size).makeImageSnapshot().toComposeImageBitmap()
    }
}

actual fun String.toAscii(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .replace(Regex("[^\\x00-\\x7F]"), "")
