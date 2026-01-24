package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.helpers.ExternalId

data class MessageStatusData(
    val userExtId: ExternalId,
    val sentAt: Long?,
    val receivedAt: Long?,
    val readAt: Long?,
) : DTO
