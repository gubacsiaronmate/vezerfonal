package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import vezerfonal.composeapp.generated.resources.filter_all
import vezerfonal.composeapp.generated.resources.inbox_title
import vezerfonal.composeapp.generated.resources.unread
import vezerfonal.composeapp.generated.resources.unread_of_connector
import vezerfonal.composeapp.generated.resources.urgent
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
    var quickFilter by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            messages = getMessages(100, client, accessToken)
        } catch (e: Exception) {
            error = e
        }
        filtered = messages
        isLoading = false

        messageFilterState.setEarliestMessageUnixTime(messages.earliestMessageTimestamp)

        if (prefStorage.getArchiveEnabled()) {
            val minStatus = prefStorage.getArchiveMinStatus()
            val delayMs = prefStorage.getArchiveDelayHours().toLong() * 3_600_000L
            val now = Clock.System.now().toEpochMilliseconds()

            val toArchive = messages.filter { msg ->
                val statusQualifies = when (minStatus) {
                    MessageStatus.received -> msg.status == MessageStatus.received || msg.status == MessageStatus.read
                    MessageStatus.read -> msg.status == MessageStatus.read
                    else -> false
                }
                statusQualifies && (now - msg.sentAt) >= delayMs
            }

            val archived = toArchive.filter { msg ->
                try {
                    sendInteraction(
                        client, accessToken,
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

    // Quick-filter the base list, then pass to advanced filter
    val quickFiltered = remember(messages, quickFilter) {
        when (quickFilter) {
            "unread" -> messages.filter { it.status != MessageStatus.read }
            "urgent" -> messages.filter { it.isUrgent }
            else -> messages
        }
    }

    Scaffold(
        topBar = {
            Surface(color = MaterialTheme.colorScheme.surface) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                ) {
                    // Title row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.xl, vertical = Spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(Res.string.inbox_title),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            if (messages.isNotEmpty()) {
                                Text(
                                    text = "$unreadCount ${stringResource(Res.string.unread_of_connector)} ${messages.size}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                        // Advanced filter icon
                        val activeFilterCount = listOf(
                            messageFilterState.senderName.isNotEmpty(),
                            messageFilterState.isImportant,
                            messageFilterState.isWaitingForAnswer,
                            messageFilterState.searchQuery.isNotEmpty(),
                            messageFilterState.tagSelectionState.selectedItems.isNotEmpty(),
                            messageFilterState.selectedStartDate != 0L || messageFilterState.selectedEndDate != 0L,
                        ).count { it }
                        BadgedBox(badge = {
                            if (activeFilterCount > 0)
                                Badge { Text(activeFilterCount.toString()) }
                        }) {
                            IconButton(onClick = { isFilterOpened = !isFilterOpened }) {
                                Icon(
                                    imageVector = Icons.Outlined.FilterList,
                                    contentDescription = "Filter",
                                    tint = if (activeFilterCount > 0)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }

                    // Quick filter chips
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = Spacing.lg),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                        modifier = Modifier.padding(bottom = Spacing.sm),
                    ) {
                        item {
                            FilterChip(
                                selected = quickFilter == null,
                                onClick = { quickFilter = null; filtered = messages },
                                label = { Text(stringResource(Res.string.filter_all)) },
                            )
                        }
                        item {
                            FilterChip(
                                selected = quickFilter == "unread",
                                onClick = {
                                    quickFilter = if (quickFilter == "unread") null else "unread"
                                    filtered = if (quickFilter == "unread")
                                        messages.filter { it.status != MessageStatus.read }
                                    else messages
                                },
                                label = { Text(stringResource(Res.string.unread)) },
                            )
                        }
                        item {
                            FilterChip(
                                selected = quickFilter == "urgent",
                                onClick = {
                                    quickFilter = if (quickFilter == "urgent") null else "urgent"
                                    filtered = if (quickFilter == "urgent")
                                        messages.filter { it.isUrgent }
                                    else messages
                                },
                                label = { Text(stringResource(Res.string.urgent)) },
                            )
                        }
                    }

                    // Advanced filter controls (when open)
                    if (isFilterOpened) {
                        FilterRow(
                            onFilterOpened = { isFilterOpened = true },
                            onCompleted = { result ->
                                filtered = result
                                isFilterOpened = false
                            },
                            isFilterOpened = isFilterOpened,
                            messages = quickFiltered,
                            state = messageFilterState,
                        )
                    }

                    if (isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
                    else HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ScrollableMessageList(
                isSwipeable = true,
                messages = filtered,
                onMessageClick = onMessageClick,
                onArchive = { message ->
                    filtered = filtered.filter { it != message }
                    scope.launch {
                        try {
                            sendInteraction(
                                client, accessToken,
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
                            messageFilterState.updateTagSelectionState(
                                TagSelectionStateModel(selectedItems = tags.toSet())
                            )
                        }
                    )
            }

            if (error != null) ErrorDialog(error!!, Modifier.align(Alignment.Center))
        }
    }
}
