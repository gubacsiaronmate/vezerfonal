package com.smokinggunstudio.vezerfonal.ui.helpers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.caverock.androidsvg.SVG
import androidx.core.graphics.createBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.Image
import java.io.ByteArrayOutputStream
import java.text.Normalizer
import kotlin.math.max

actual fun FileData.svgXMLToByteArray(size: Int, quality: Int): ImageBitmap {
    val svg = SVG.getFromString(this.bytes.decodeToString())
    
    val viewBox = svg.documentViewBox
        ?: RectF(0f, 0f, size.toFloat(), size.toFloat())
    
    val scale = max(
        size / viewBox.width(),
        size / viewBox.height()
    )
    
    val bitmap = createBitmap(size, size)
    val canvas = Canvas(bitmap)
    
    canvas.scale(scale, scale)
    canvas.translate(
        -viewBox.left,
        -viewBox.top
    )
    
    svg.renderToCanvas(canvas)
    
    return bitmap.asImageBitmap()
}

actual fun String.toAscii(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .replace(Regex("[^\\x00-\\x7F]"), "")