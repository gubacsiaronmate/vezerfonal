package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.between
import com.smokinggunstudio.vezerfonal.ui.state.controller.MessageFilterStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.MessageFilterStateModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable fun FilterRow(
    onFilterOpened: ClickEvent,
    onCompleted: CallbackEvent<List<MessageData>>,
    isFilterOpened: Boolean,
    messages: List<MessageData>,
    snapshot: MessageFilterStateModel,
) {
    val state = remember { MessageFilterStateController(snapshot) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        if (!isFilterOpened) FilterButton(onFilterOpened)
        else FilterApplyCancelButtons(
            onApply = {
                val filtered = messages.filter { message ->
                    val dateMatch = Instant.fromEpochMilliseconds(message.sentAt).between(
                        start = Instant.fromEpochMilliseconds(state.selectedStartDate),
                        end = Instant.fromEpochMilliseconds(state.selectedEndDate)
                    )
                    
                    val senderMatch = if (state.senderName.isNotEmpty())
                        message.author.name.contains(state.senderName, ignoreCase = true)
                    else true
                    
                    val urgentMatch =
                        if (state.isImportant)
                            message.isUrgent else true
                    
                    val searchMatch = if (state.searchQuery.isNotEmpty()) {
                        message.title.contains(state.searchQuery, ignoreCase = true) ||
                        message.content.contains(state.searchQuery, ignoreCase = true)
                    } else true
                    
                    dateMatch && senderMatch && urgentMatch && searchMatch
                }
                onCompleted(filtered)
            },
            onCancel = {
                state.clear()
                onCompleted(messages)
            }
        )
    }
}