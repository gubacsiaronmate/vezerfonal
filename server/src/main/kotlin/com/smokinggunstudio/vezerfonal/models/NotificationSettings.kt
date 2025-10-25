package com.smokinggunstudio.vezerfonal.models



data class NotificationSettings(
    val id: Int?,
    val user: User,
    val allowPush: Boolean = true,
    val allowAlarm: Boolean = true
)