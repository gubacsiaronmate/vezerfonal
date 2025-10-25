package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime


data class Membership(
    val user: User,
    val joinedAt: LocalDateTime
)
