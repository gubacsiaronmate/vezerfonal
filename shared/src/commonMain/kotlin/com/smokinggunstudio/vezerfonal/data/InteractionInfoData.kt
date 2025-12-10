package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
data class InteractionInfoData(
    val messageExtId: String,
    val type: InteractionType,
    val status: MessageStatus?,
    val reaction: String?,
    val recipientIdentifier: String?,
) : DTO {
    constructor(
        messageExtId: String,
        type: InteractionType,
        status: MessageStatus?,
    ) : this(
        messageExtId = messageExtId,
        type = type,
        status = status,
        reaction = null,
        recipientIdentifier = null,
    )
    
    constructor(
        messageExtId: String,
        type: InteractionType,
        reaction: String? = null,
        recipientIdentifier: String? = null,
    ) : this(
        messageExtId,
        type,
        status = null,
        reaction = reaction,
        recipientIdentifier = recipientIdentifier
    )
    
    constructor(
        messageExtId: String,
        type: InteractionType,
    ) : this(
        messageExtId = messageExtId,
        type = type,
        status = null,
        reaction = null,
        recipientIdentifier = null,
    )
    
    override fun toSerializable(): Map<String, Any?> {
        return mapOf(
            "messageExtId" to messageExtId,
            "type" to type,
            "status" to status,
            "reaction" to reaction,
            "recipientIdentifier" to recipientIdentifier,
        )
    }
}
