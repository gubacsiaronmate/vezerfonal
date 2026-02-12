package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData
import org.jetbrains.skia.Image

actual fun FileData.toImageResource(): ImageBitmap =
    Image.makeFromEncoded(bytes).toComposeImageBitmap()