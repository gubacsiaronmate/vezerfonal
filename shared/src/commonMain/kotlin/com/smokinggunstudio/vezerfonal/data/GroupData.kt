package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class GroupData(
    override val name: String,
    val description: String,
    val members: List<String>,
    val adminIdentifier: String
) : DTO
