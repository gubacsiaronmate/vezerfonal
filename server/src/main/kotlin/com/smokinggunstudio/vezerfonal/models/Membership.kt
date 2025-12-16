package com.smokinggunstudio.vezerfonal.models

import kotlin.time.ExperimentalTime
import kotlin.time.Instant


data class Membership @OptIn(ExperimentalTime::class) constructor(
    val user: User,
    val groupId: Int?,
    val joinedAt: Instant
)
