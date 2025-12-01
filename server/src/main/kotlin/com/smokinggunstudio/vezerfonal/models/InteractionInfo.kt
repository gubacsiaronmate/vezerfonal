package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import kotlinx.datetime.LocalDateTime

data class InteractionInfo(
    val id: Int?,
    val message: Message,
    val user: User,
    val type: InteractionType,
    val status: MessageStatus?,
    val reaction: String?,
    val actor: User?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
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
        actor = null,
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
        actor = null,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
    
    constructor(
        message: Message,
        user: User,
        type: InteractionType,
        actor: User
    ) : this(
        id = null,
        message = message,
        user = user,
        type = type,
        status = null,
        reaction = null,
        actor = actor,
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
        actor = null,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
}