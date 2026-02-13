package com.smokinggunstudio.vezerfonal.ui.helpers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.RectF
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.caverock.androidsvg.SVG
import androidx.core.graphics.createBitmap
import com.caverock.androidsvg.PreserveAspectRatio
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.Image
import java.io.ByteArrayOutputStream
import java.text.Normalizer
import kotlin.math.max

actual fun FileData.svgXMLToByteArray(size: Int, quality: Int): ImageBitmap {
    val svg = SVG.getFromString(this.bytes.decodeToString())
    
    svg.setDocumentPreserveAspectRatio(PreserveAspectRatio.STRETCH)
    svg.setDocumentWidth(size.toFloat())
    svg.setDocumentHeight(size.toFloat())
    
    val bitmap = createBitmap(size, size)
    val canvas = Canvas(bitmap)
    
    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    
    svg.renderToCanvas(canvas)
    
    return bitmap.asImageBitmap()
}

actual fun String.toAscii(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .replace(Regex("[^\\x00-\\x7F]"), "")