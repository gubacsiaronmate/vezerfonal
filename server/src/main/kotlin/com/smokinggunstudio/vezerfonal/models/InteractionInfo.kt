package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class InteractionInfo @OptIn(ExperimentalTime::class) constructor(
    val id: Int?,
    val message: Message,
    val user: User,
    val type: InteractionType,
    val status: MessageStatus?,
    val reaction: String?,
    val recipient: User?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val deletedAt: Instant?
) {
    constructor(
        message: Message,
        user: User,
        type: InteractionType,
        status: MessageStatus?,
    ) : this(
        id = null,
        message = message,
        user = user,
        type = type,
        status = status ?: MessageStatus.sent,
        reaction = null,
        recipient = null,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
    
    constructor(
        message: Message,
        user: User,
        type: InteractionType,
        reaction: String
    ) : this(
        id = null,
        message = message,
        user = user,
        type = type,
        status = null,
        reaction = reaction,
        recipient = null,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
    
    constructor(
        message: Message,
        user: User,
        type: InteractionType,
        recipient: User
    ) : this(
        id = null,
        message = message,
        user = user,
        type = type,
        status = null,
        reaction = null,
        recipient = recipient,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
    
    constructor(
        message: Message,
        user: User,
        type: InteractionType,
    ) : this(
        id = null,
        message = message,
        user = user,
        type = type,
        status = null,
        reaction = null,
        recipient = null,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
    
    fun toDTO() = InteractionInfoData(
        messageExtId = message.externalId,
        type = type,
        status = status,
        reaction = reaction,
        recipientIdentifier = recipient?.identifier,
    )
}