package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.getMessages
import com.smokinggunstudio.vezerfonal.network.api.subscribeToMessages
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.*
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import io.ktor.client.*
import io.ktor.client.network.sockets.SocketTimeoutException
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

        messageFilterState.setEarliestMessageUnixTime(
            messages
                .minByOrNull {
                    LocalDateTime.parse(it.sentAt)
                }
                ?.sentAt
                .toLDTOrNull()
        )
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
    
    Box(Modifier.fillMaxWidth()) {
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
            else HorizontalDivider(Modifier.fillMaxWidth().height(1.dp))
            
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
        if (error != null) ErrorDialog(error!!.message!!, true)
    }
}