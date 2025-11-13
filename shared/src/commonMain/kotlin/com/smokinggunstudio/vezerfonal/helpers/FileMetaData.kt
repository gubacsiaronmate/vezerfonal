package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.serialization.Serializable

@Serializable
data class FileMetaData(
    val name: String,
    val mimeType: String,
    val fileType: String = name.substringAfterLast(".").lowercase()
)
