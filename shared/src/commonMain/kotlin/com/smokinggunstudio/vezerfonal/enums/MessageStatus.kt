package com.smokinggunstudio.vezerfonal.enums

import kotlinx.serialization.Serializable

@Serializable
enum class MessageStatus {
    sent,
    received,
    read,
}