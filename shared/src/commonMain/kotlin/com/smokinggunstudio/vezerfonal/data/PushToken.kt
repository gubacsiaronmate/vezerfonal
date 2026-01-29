package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class PushToken(
    val token: String,
    val platform: String
) : DTO