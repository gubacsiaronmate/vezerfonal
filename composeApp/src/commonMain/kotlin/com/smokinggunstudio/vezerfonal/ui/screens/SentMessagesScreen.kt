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
import com.smokinggunstudio.vezerfonal.network.api.getSentMessages
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.earliestMessageTimestamp
import com.smokinggunstudio.vezerfonal.ui.state.controller.MessageFilterStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.MessageFilterStateModel
import io.ktor.client.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable fun SentMessagesScreen(
    client: HttpClient,
    accessToken: String,
    onMessageClick: CallbackFunction<MessageData>,
) {
    var isLoading by remember { mutableStateOf(false) }
    var isFilterOpened by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    val messageFilterState = remember { MessageFilterStateController(MessageFilterStateModel()) }
    var isTagSelectTabOpened by remember { mutableStateOf(false) }
    var messages by remember { mutableStateOf<List<MessageData>>(emptyList()) }
    var filtered by remember(messages) { mutableStateOf(messages)}
    
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            messages = getSentMessages(client, accessToken)
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
                snapshot = messageFilterState.snapshot()
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
                        snapshot = messageFilterState.snapshot(),
                        tabOpenedClick = { isTagSelectTabOpened = true },
                        modifier = Modifier.align(Alignment.TopCenter)
                    ) { _ -> }
                
                if (isTagSelectTabOpened)
                    TagSelect(
                        snapshot = messageFilterState.tagSelectionState,
                        onCancelClick = { isTagSelectTabOpened = false },
                        onApplyClick = { }
                    )
            }
        }
        
        if (error != null) ErrorDialog(error!!, Modifier.align(Alignment.Center))
    }
}