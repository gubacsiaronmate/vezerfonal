package com.smokinggunstudio.vezerfonal.helpers

class TwoFactorRequiredException(
    val email: String,
    val orgExternalId: String,
    val rememberMe: Boolean,
) : Exception("Two-factor authentication required.")
