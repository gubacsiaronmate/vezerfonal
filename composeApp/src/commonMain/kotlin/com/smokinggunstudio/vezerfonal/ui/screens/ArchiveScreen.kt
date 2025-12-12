package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.getMessages
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
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
import com.smokinggunstudio.vezerfonal.ui.helpers.toLDTOrNull
import com.smokinggunstudio.vezerfonal.ui.helpers.toLocalDateTime
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import io.ktor.client.HttpClient
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable fun ArchiveScreen(
    client: HttpClient,
    accessToken: String,
    onMessageClick: CallbackEvent<MessageData>,
    scrollLockedBySliderCallback: CallbackEvent<Boolean>
) {
    var isLoading by remember { mutableStateOf(false) }
    var isFilterOpened by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    val messageFilterState = remember { MessageFilterState() }
    var isTagSelectTabOpened by remember { mutableStateOf(false) }
    var messages by remember { mutableStateOf<List<MessageData>>(emptyList()) }
    var filtered by remember(messages) { mutableStateOf<List<MessageData>>(emptyList())}
    
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            messages = getMessages(100, client, accessToken)
        } catch (e: Exception) {
            error = e
        }
        filtered = messages
        isLoading = false
        
        messageFilterState.setEarliestMessageUnixTime(
            messages
                .minByOrNull {
                    LocalDateTime.parse(it.sentAt)
                }
                ?.sentAt
                .toLDTOrNull()
        )
    }
    
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        Column {
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
            if (isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
            else HorizontalDivider(Modifier.height(1.dp))
            ScrollableMessageList(
                isSwipeable = false,
                messages = filtered,
                onMessageClick = onMessageClick,
                onArchive = {}
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
        
        if (error != null)
            ErrorDialog(
                errorMessage = error!!.message!!,
                isUnauthed = error is UnauthorizedException,
                modifier = Modifier.align(Alignment.Center)
            )
    }
}