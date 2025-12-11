package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.ui.components.FilterApplyCancelButtons
import com.smokinggunstudio.vezerfonal.ui.components.FilterButton
import com.smokinggunstudio.vezerfonal.ui.components.FilterRow
import com.smokinggunstudio.vezerfonal.ui.components.ListItem
import com.smokinggunstudio.vezerfonal.ui.components.MessageFilter
import com.smokinggunstudio.vezerfonal.ui.components.ScrollableMessageList
import com.smokinggunstudio.vezerfonal.ui.components.TagSelect
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.between
import com.smokinggunstudio.vezerfonal.ui.helpers.toLDT
import com.smokinggunstudio.vezerfonal.ui.helpers.toLocalDateTime
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable fun ArchiveScreen(
    messages: List<MessageData>,
    onMessageClick: CallbackEvent<MessageData>,
    scrollLockedBySliderCallback: CallbackEvent<Boolean>
) {
    var isFilterOpened by remember { mutableStateOf(false) }
    val messageFilterState = remember { MessageFilterState() }
    var isTagSelectTabOpened by remember { mutableStateOf(false) }
    var filtered by remember(messages) { mutableStateOf<List<MessageData>>(emptyList())}
    
    Column(
        modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.surface))
    {
        FilterRow(
            onFilterOpened = { isFilterOpened = true },
            onCompleted = {
                filtered = it
                isFilterOpened = false
            },
            isFilterOpened = isFilterOpened,
            messages = messages,
            messageFilterState = messageFilterState
        )
        HorizontalDivider()
        ScrollableMessageList(
            messages = filtered,
            onMessageClick = onMessageClick,
        ) {
            if (isFilterOpened)
                MessageFilter(
                    state = messageFilterState,
                    tabOpenedClick = { isTagSelectTabOpened = true },
                    modifier = Modifier.align(Alignment.TopCenter)
                ) { scrollLockedBySliderCallback(it && isFilterOpened) }
            else scrollLockedBySliderCallback(false)
            
            if (isTagSelectTabOpened)
                TagSelect(
                    state = messageFilterState.tagSelectionState,
                    onCancelClick = { isTagSelectTabOpened = false },
                    onApplyClick = { }
                )
        }
    }
}