package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.getArchivedMessages
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.earliestMessageTimestamp
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import io.ktor.client.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
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
    var filtered by remember(messages) { mutableStateOf(messages)}
    
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            messages = getArchivedMessages(client, accessToken)
        } catch (e: Exception) {
            error = e
        }
        filtered = messages
        isLoading = false
        
        messageFilterState
            .setEarliestMessageUnixTime(messages.earliestMessageTimestamp)
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