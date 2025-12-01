package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime
import java.util.UUID


data class Organisation(
    val id: Int,
    val name: String,
    val externalId: String,
    val createdAt: LocalDateTime
)