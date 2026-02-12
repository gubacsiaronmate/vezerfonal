package com.smokinggunstudio.vezerfonal.ui.helpers

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import com.caverock.androidsvg.SVG
import androidx.core.graphics.createBitmap
import java.io.ByteArrayOutputStream
import java.text.Normalizer

actual fun String.svgXMLToByteArray(size: Int, quality: Int): ByteArray {
    val svg = SVG.getFromString(this)
    
    val bitmap = createBitmap(size, size)
    val canvas = Canvas(bitmap)
    
    svg.setDocumentWidth(size.toFloat())
    svg.setDocumentHeight(size.toFloat())
    svg.renderToCanvas(canvas)
    
    val output = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
    
    return output.toByteArray()
}

actual fun String.toAscii(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .replace(Regex("[^\\x00-\\x7F]"), "")