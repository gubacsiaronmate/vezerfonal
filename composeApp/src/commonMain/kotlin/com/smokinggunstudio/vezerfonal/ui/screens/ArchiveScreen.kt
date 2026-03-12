package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.network.api.getArchivedMessages
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.ContentContainer
import com.smokinggunstudio.vezerfonal.ui.helpers.earliestMessageTimestamp
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.archive
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable fun ArchiveScreen(
    accessToken: String,
    tagList: List<TagData>,
    onMessageClick: CallbackFunction<MessageData>,
    scrollLockedBySliderCallback: CallbackFunction<Boolean>
) {
    val client = LocalHttpClient.current
    var isLoading by remember { mutableStateOf(false) }
    var isFilterOpened by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    val messageFilterState = remember { MessageFilterState(tagList) }
    var isTagSelectTabOpened by remember { mutableStateOf(false) }
    var messages by remember { mutableStateOf<List<MessageData>>(emptyList()) }
    var filtered by remember(messages) { mutableStateOf(messages) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            messages = getArchivedMessages(client, accessToken)
        } catch (e: Exception) {
            error = e
        }
        filtered = messages
        isLoading = false
        messageFilterState.setEarliestMessageUnixTime(messages.earliestMessageTimestamp)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.archive),
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            ContentContainer {
                FilterRow(
                    onFilterOpened = { isFilterOpened = true },
                    onCompleted = {
                        filtered = it
                        isFilterOpened = false
                    },
                    isFilterOpened = isFilterOpened,
                    messages = messages,
                    state = messageFilterState,
                )
                if (isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
                else HorizontalDivider(Modifier.height(1.dp))
                ScrollableMessageList(
                    isSwipeable = false,
                    messages = filtered,
                    onMessageClick = onMessageClick,
                ) {
                    if (isFilterOpened)
                        MessageFilter(
                            state = messageFilterState,
                            tabOpenedClick = { isTagSelectTabOpened = true },
                            modifier = Modifier.align(Alignment.TopCenter),
                        ) { scrollLockedBySliderCallback(it && isFilterOpened) }
                    else scrollLockedBySliderCallback(false)

                    if (isTagSelectTabOpened && isFilterOpened)
                        TagSelect(
                            snapshot = messageFilterState.tagSelectionState,
                            onCancelClick = { isTagSelectTabOpened = false },
                            onApplyClick = { tags ->
                                messageFilterState.updateTagSelectionState(
                                    TagSelectionStateModel(selectedItems = tags.toSet())
                                )
                            }
                        )
                }
            }

            if (error != null) ErrorDialog(error!!, Modifier.align(Alignment.Center))
        }
    }
}
