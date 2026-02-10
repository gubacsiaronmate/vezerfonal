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
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.between
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable fun FilterRow(
    onFilterOpened: Function,
    onCompleted: CallbackFunction<List<MessageData>>,
    isFilterOpened: Boolean,
    messages: List<MessageData>,
    state: MessageFilterState,
) {
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
                    val dateMatch =
                        if (state.selectedStartDate == 0L && state.selectedEndDate == 0L) true
                        else Instant.fromEpochMilliseconds(message.sentAt).between(
                            start = Instant.fromEpochMilliseconds(state.selectedStartDate),
                            end = Instant.fromEpochMilliseconds(state.selectedEndDate)
                        )
                    
                    val senderMatch = if (state.senderName.isNotEmpty())
                        message.author.name.contains(state.senderName, ignoreCase = true)
                    else true
                    
                    val urgentMatch =
                        if (!state.isImportant) true
                        else message.isUrgent
                    
                    val waitingForAnswerMatch = if (state.isWaitingForAnswer) {
                        message.reactedWith == null || message.status != MessageStatus.read
                    } else true
                    
                    val searchMatch = if (state.searchQuery.isNotEmpty()) {
                        message.title.contains(state.searchQuery, ignoreCase = true) ||
                        message.content.contains(state.searchQuery, ignoreCase = true)
                    } else true
                    
                    val tagMatch = if (state.tagSelectionState.selectedItems.isEmpty()) true
                    else message.tags.containsAll(state.tagSelectionState.selectedItems.map { it.name })
                    
                    dateMatch && senderMatch && urgentMatch && waitingForAnswerMatch && searchMatch && tagMatch
                }
                onCompleted(filtered.sortedBy { it.sentAt })
            },
            onCancel = {
                state.clear()
                onCompleted(messages.sortedBy { it.sentAt })
            }
        )
    }
}