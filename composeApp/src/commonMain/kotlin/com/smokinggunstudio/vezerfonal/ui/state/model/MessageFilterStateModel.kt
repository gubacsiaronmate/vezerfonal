package com.smokinggunstudio.vezerfonal.ui.state.model

import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class MessageFilterStateModel(
    val earliestMessageUnixTime: Float = 0F,
    val latestMessageUnixTime: Float = Clock.System.now().toEpochMilliseconds().toFloat(),
    val selectedStartDate: Long = 0L,
    val selectedEndDate: Long = 0L,
    val senderName: String = "",
    val isImportant: Boolean = false,
    val isWaitingForAnswer: Boolean = false,
    val searchQuery: String = "",
    val tagSelectionState: TagSelectionStateModel = TagSelectionStateModel()
)
