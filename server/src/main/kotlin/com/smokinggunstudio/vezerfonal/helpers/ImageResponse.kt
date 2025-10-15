package com.smokinggunstudio.vezerfonal.helpers

import io.ktor.http.*
import java.io.File

data class ImageResponse(
    val file: File,
    val fileType: ContentType
)
