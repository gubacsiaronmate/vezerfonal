package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Organisation(
    val name: String,
    val createdAt: LocalDateTime
)