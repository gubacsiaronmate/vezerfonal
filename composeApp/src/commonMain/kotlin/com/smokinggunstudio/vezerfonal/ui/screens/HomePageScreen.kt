package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.network.api.getMessages
import com.smokinggunstudio.vezerfonal.network.api.subscribeToMessages
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.*
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import io.ktor.client.*
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.spiralgraphic
import vezerfonal.composeapp.generated.resources.vezerfonal
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun HomePageScreen(
    accessToken: String,
    client: HttpClient,
    onMessageClick: CallbackEvent<MessageData>,
    scrollLockedBySliderCallback: CallbackEvent<Boolean>
) {
    val scope = rememberCoroutineScope()
    var isFilterOpened by remember { mutableStateOf(false) }
    val messageFilterState = remember { MessageFilterState() }
    var messages by remember { mutableStateOf<List<MessageData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var isTagSelectTabOpened by remember { mutableStateOf(false) }
    var filtered by remember(messages) { mutableStateOf(messages) }
    var timedOut by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isLoading = true
        messages = getMessages(100, client, accessToken)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                stringResource(Res.string.vezerfonal),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        
        Image(
            painter = painterResource(Res.drawable.spiralgraphic),
            contentDescription = "Home Page Image",
            modifier = Modifier.fillMaxWidth()
                .height(210.dp),
            contentScale = ContentScale.FillWidth
        )
        
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (!isFilterOpened) FilterButton { isFilterOpened = true }
            else FilterApplyCancelButtons(
                onApply = {
                    filtered = messages.filter { message ->
                        val dateMatch = if (messageFilterState.selectedStartDate > 0 && messageFilterState.selectedEndDate > 0) {
                            message.sentAt.toLDT().between(
                                start = messageFilterState.selectedStartDate.toLocalDateTime(),
                                end = messageFilterState.selectedEndDate.toLocalDateTime()
                            )
                        } else true
                        
                        val senderMatch = if (messageFilterState.senderName.isNotEmpty()) {
                            message.author.name.contains(messageFilterState.senderName, ignoreCase = true)
                        } else true
                        
                        val urgentMatch =
                            if (messageFilterState.isImportant)
                                message.isUrgent else true
                        
                        val searchMatch = if (messageFilterState.searchQuery.isNotEmpty()) {
                            message.title.contains(messageFilterState.searchQuery, ignoreCase = true)
                                    || message.content.contains(messageFilterState.searchQuery, ignoreCase = true)
                        } else true
                        
                        dateMatch && senderMatch && urgentMatch && searchMatch
                    }
                    isFilterOpened = false
                },
                onCancel = {
                    filtered = messages
                    isFilterOpened = false
                }
            )
        }
        
        if (isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
        else HorizontalDivider(Modifier.fillMaxWidth().height(1.dp))
        
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                LazyColumn(modifier = Modifier.weight(1F)) {
                    items(filtered.reversed()) { message ->
                        ListItem(
                            title = message.title,
                            author = message.author.name,
                            onClick = { onMessageClick(message) }
                        )
                    }
                }
            }
            
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