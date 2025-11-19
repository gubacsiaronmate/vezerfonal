package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class MessageData(
    val title: String,
    val author: String,
    val content: String,
    val isUrgent: Boolean,
    val tags: List<String>,
    val userIdentifiers: List<String>?,
    val groudAdminIdentifiers: List<String>?
)
