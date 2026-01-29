package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import kotlinx.serialization.Serializable

@Serializable
data class MessageStatusData(
    val userExtId: ExternalId,
    val sentAt: Long?,
    val receivedAt: Long?,
    val readAt: Long?,
) : DTO
