package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.enums.NotificationType
import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    val title: String,
    val notifType: NotificationType,
    val data: Map<String, String>
) : DTO