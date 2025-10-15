package com.smokinggunstudio.vezerfonal.models

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: Int,
    val tagName: String,
    val messageIds: List<Int>
)
