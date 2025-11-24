package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime


data class Membership(
    val user: User,
    val groupId: Int?,
    val joinedAt: LocalDateTime
)
