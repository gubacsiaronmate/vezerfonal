package com.smokinggunstudio.vezerfonal.ui.helpers

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData

actual fun FileData.toImageResource(): ImageBitmap =
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()