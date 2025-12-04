package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class TagData(
    override val name: String
) : DTO
