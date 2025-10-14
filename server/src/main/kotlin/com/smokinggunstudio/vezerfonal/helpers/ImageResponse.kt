package com.smokinggunstudio.vezerfonal.helpers

import io.ktor.http.ContentType
import java.io.File

import kotlinx.serialization.Serializable

//@Serializable
data class ImageResponse(
    val file: File,
    val fileType: ContentType
)
