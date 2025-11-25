package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
sealed interface DTO {
    val name: String
}