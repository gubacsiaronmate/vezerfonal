package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime


data class Organisation(
    val name: String,
    val createdAt: LocalDateTime
)