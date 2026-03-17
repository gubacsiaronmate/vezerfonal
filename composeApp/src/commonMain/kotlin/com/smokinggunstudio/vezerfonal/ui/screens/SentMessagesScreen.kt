package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.getSentMessages
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.earliestMessageTimestamp
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import kotlin.time.ExperimentalTime
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.sent_messages

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable fun SentMessagesScreen(
    accessToken: String,
    tagList: List<TagData>,
    onMessageClick: CallbackFunction<MessageData>,
) {
    val client = LocalHttpClient.current
    var isLoading by remember { mutableStateOf(false) }
    var isFilterOpened by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    val messageFilterState = remember { MessageFilterState(tagList) }
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
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.sent_messages),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            Column {
                FilterRow(
                    onFilterOpened = { isFilterOpened = true },
                    onCompleted = {
                        filtered = it
                        isFilterOpened = false
                    },
                    isFilterOpened = isFilterOpened,
                    messages = messages,
                    state = messageFilterState
                )
                if (isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
                else HorizontalDivider()
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
                            modifier = Modifier.align(Alignment.TopCenter),
                        ) { _ -> }

                    if (isTagSelectTabOpened && isFilterOpened)
                        TagSelect(
                            snapshot = messageFilterState.tagSelectionState,
                            onCancelClick = { isTagSelectTabOpened = false },
                            onApplyClick = { tags ->
                                messageFilterState
                                    .updateTagSelectionState(
                                        TagSelectionStateModel(
                                            selectedItems = tags.toSet()
                                        )
                                    )
                            }
                        )
                }
            }

            if (error != null) ErrorDialog(error!!, Modifier.align(Alignment.Center))
        }
    }
}