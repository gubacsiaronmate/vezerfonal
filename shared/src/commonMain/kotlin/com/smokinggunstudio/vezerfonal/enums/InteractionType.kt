package com.smokinggunstudio.vezerfonal.enums

import kotlinx.serialization.Serializable

@Serializable
enum class InteractionType {
    status,
    reaction,
    nudge,
    archive
}