package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmInline
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
@OptIn(ExperimentalTime::class)
data class InteractionInfoData(
    val userIdentifier: String,
    val messageExtId: String,
    val type: InteractionType,
    val status: MessageStatus?,
    val reaction: String?,
    val recipientIdentifier: String?,
    val createdAt: Long,
) : DTO {
    constructor(
        userIdentifier: String,
        messageExtId: String,
        type: InteractionType,
        status: MessageStatus?,
    ) : this(
        userIdentifier = userIdentifier,
        messageExtId = messageExtId,
        type = type,
        status = status,
        reaction = null,
        recipientIdentifier = null,
        createdAt = Clock.System.now().toEpochMilliseconds(),
    )
    
    constructor(
        userIdentifier: String,
        messageExtId: String,
        type: InteractionType,
        reaction: String? = null,
        recipientIdentifier: String? = null,
    ) : this(
        userIdentifier = userIdentifier,
        messageExtId = messageExtId,
        type = type,
        status = null,
        reaction = reaction,
        recipientIdentifier = recipientIdentifier,
        createdAt = Clock.System.now().toEpochMilliseconds(),
    )
    
    constructor(
        userIdentifier: String,
        messageExtId: String,
        type: InteractionType,
    ) : this(
        userIdentifier = userIdentifier,
        messageExtId = messageExtId,
        type = type,
        status = null,
        reaction = null,
        recipientIdentifier = null,
        createdAt = Clock.System.now().toEpochMilliseconds(),
    )
    
    override fun toSerialized(): String {
        return Json.encodeToString(this)
    }
}
