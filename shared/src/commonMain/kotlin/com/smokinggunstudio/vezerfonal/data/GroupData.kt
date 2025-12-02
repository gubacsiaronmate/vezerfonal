package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

private typealias Identifier = String

@Serializable
data class GroupData(
    override val name: String,
    val description: String,
    val members: List<Identifier>,
    val adminIdentifier: Identifier
) : DTO
