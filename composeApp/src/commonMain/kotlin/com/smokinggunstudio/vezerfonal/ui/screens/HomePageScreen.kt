package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.LocalPreferenceStorage
import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.getMessages
import com.smokinggunstudio.vezerfonal.network.api.sendInteraction
import com.smokinggunstudio.vezerfonal.network.api.subscribeToMessages
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.earliestMessageTimestamp
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import io.ktor.client.network.sockets.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Clock
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.inbox_title
import vezerfonal.composeapp.generated.resources.vezerfonal
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(
    accessToken: String,
    userIdentifier: String,
    tagList: List<TagData>,
    darkModeState: MutableState<Boolean?>,
    onMessageClick: CallbackFunction<MessageData>,
    scrollLockedBySliderCallback: CallbackFunction<Boolean>,
) {
    val client = LocalHttpClient.current
    val prefStorage = LocalPreferenceStorage.current
    val scope = rememberCoroutineScope()
    var isFilterOpened by remember { mutableStateOf(false) }
    val messageFilterState = remember { MessageFilterState(tagList) }
    var messages by remember { mutableStateOf<List<MessageData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var isTagSelectTabOpened by remember { mutableStateOf(false) }
    var filtered by remember(messages) { mutableStateOf(messages) }
    var timedOut by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    
    
    
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            messages = getMessages(100, client, accessToken)
        } catch (e: Exception) {
            error = e
        }
        filtered = messages
        isLoading = false

        messageFilterState
            .setEarliestMessageUnixTime(messages.earliestMessageTimestamp)

        if (prefStorage.getArchiveEnabled()) {
            val minStatus = prefStorage.getArchiveMinStatus()
            val delayMs = prefStorage.getArchiveDelayHours().toLong() * 3_600_000L
            val now = Clock.System.now().toEpochMilliseconds()

            val toArchive = messages.filter { msg ->
                msg.status.ordinal >= minStatus.ordinal && (now - msg.sentAt) >= delayMs
            }

            val archived = toArchive.filter { msg ->
                try {
                    sendInteraction(
                        client,
                        accessToken,
                        InteractionInfoData(
                            userIdentifier = userIdentifier,
                            messageExtId = msg.externalId,
                            type = InteractionType.archive,
                        ),
                    )
                } catch (_: Exception) { false }
            }

            if (archived.isNotEmpty()) {
                messages = messages.filter { it !in archived }
                filtered = filtered.filter { it !in archived }
            }
        }
    }
    
    try {
        scope.launch {
            while (timedOut) {
                subscribeToMessages(
                    client = client,
                    accessToken = accessToken,
                    onMessage = { messages += it },
                    onError = { e ->
                        if (e !is SocketTimeoutException) throw e
                        else timedOut = true
                    }
                )
                delay(5000)
            }
        }
    } catch (e: UnauthorizedException) {
        error = e
    }
    
    val unreadCount = messages.count { it.status != MessageStatus.read }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.vezerfonal),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                HomeGreetingHeader(
                    messageCount = messages.size,
                    unreadCount = unreadCount,
                )

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

                Box(Modifier.weight(1f)) {
                    ScrollableMessageList(
                        isSwipeable = true,
                        messages = filtered,
                        onMessageClick = onMessageClick,
                        onArchive = { message ->
                            filtered = filtered.filter { it != message }

                            scope.launch {
                                try {
                                    sendInteraction(
                                        client,
                                        accessToken,
                                        InteractionInfoData(
                                            userIdentifier = userIdentifier,
                                            messageExtId = message.externalId,
                                            type = InteractionType.archive,
                                        ),
                                    )
                                } catch (e: Exception) {
                                    error = e
                                }
                            }
                        }
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
            }

            if (error != null) ErrorDialog(error!!, Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun HomeGreetingHeader(
    messageCount: Int,
    unreadCount: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = Spacing.lg, vertical = Spacing.lg)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = stringResource(Res.string.inbox_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (messageCount > 0) {
                    Spacer(Modifier.height(Spacing.xs))
                    Text(
                        text = "$unreadCount / $messageCount",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (unreadCount > 0) {
                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                    Text(
                        text = unreadCount.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}