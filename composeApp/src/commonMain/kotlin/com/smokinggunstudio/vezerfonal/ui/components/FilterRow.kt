package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.between
import com.smokinggunstudio.vezerfonal.ui.helpers.toLDT
import com.smokinggunstudio.vezerfonal.ui.helpers.toLocalDateTime
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState

@Composable fun FilterRow(
    onFilterOpened: ClickEvent,
    onCompleted: CallbackEvent<List<MessageData>>,
    isFilterOpened: Boolean,
    messages: List<MessageData>,
    messageFilterState: MessageFilterState,
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
                    val isRangeSet = messageFilterState.selectedStartDate > 0L && messageFilterState.selectedEndDate > 0L
                    val dateMatch = if (isRangeSet)
                        message.sentAt.toLDT().between(
                            start = messageFilterState.selectedStartDate.toLocalDateTime(),
                            end = messageFilterState.selectedEndDate.toLocalDateTime()
                        )
                    else true
                    
                    val senderMatch = if (messageFilterState.senderName.isNotEmpty())
                        message.author.name.contains(messageFilterState.senderName, ignoreCase = true)
                    else true
                    
                    val urgentMatch =
                        if (messageFilterState.isImportant)
                            message.isUrgent else true
                    
                    val searchMatch = if (messageFilterState.searchQuery.isNotEmpty()) {
                        message.title.contains(messageFilterState.searchQuery, ignoreCase = true) ||
                                message.content.contains(messageFilterState.searchQuery, ignoreCase = true)
                    } else true
                    
                    dateMatch && senderMatch && urgentMatch && searchMatch
                }
                onCompleted(filtered)
            },
            onCancel = {
                messageFilterState.clear()
                onCompleted(messages)
            }
        )
    }
}