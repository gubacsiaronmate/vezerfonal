package com.smokinggunstudio.vezerfonal.models

data class PushToken(
    val id: Int,
    val user: User,
    val token: String,
    val platform: String
)
