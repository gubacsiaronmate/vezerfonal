package com.smokinggunstudio.vezerfonal.models

data class OAuth(
    val id: Int,
    val user: User,
    val providerName: String,
    val providerUserId: String
)