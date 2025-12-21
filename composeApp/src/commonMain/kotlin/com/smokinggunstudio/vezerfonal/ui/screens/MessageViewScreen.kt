package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.UserInteractionData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.network.api.getReactionsAndUsersByMessageExtId
import com.smokinggunstudio.vezerfonal.network.api.sendInteraction
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.capitalize
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageViewScreen(
    accessToken: String,
    isArchived: Boolean,
    messageStr: String,
    isSenderView: Boolean,
    userIdentifier: String,
) {
    val message = messageStr.toDTO<MessageData>()
    var error by remember { mutableStateOf<Throwable?>(null) }
    val scope = rememberCoroutineScope()
    val client = LocalHttpClient.current
    var top by remember { mutableStateOf(80.dp) }
    val statusAsStr = (message.status ?: MessageStatus.received).toString().capitalize()
    val statusString = "${stringResource(Res.string.status)}: $statusAsStr"
    var selectedReaction by remember { mutableStateOf<String?>(null) }
    var reactionsAndUsers by remember { mutableStateOf<List<UserInteractionData>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    
    if (isSenderView)
        LaunchedEffect(Unit) {
            loading = true
            reactionsAndUsers = try {
                getReactionsAndUsersByMessageExtId(
                    client = client,
                    accessToken = accessToken,
                    messageExtId = message.externalId
                )
            } catch (e: Exception) {
                error = e
                emptyList()
            }
            loading = false
        }
    
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(color = MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            maxLines = 1,
                            text = message.title,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineLarge,
                        )
                        
                        Icon(
                            imageVector =
                                if (message.isUrgent) Icons.Filled.Error
                                else Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(32.dp).fillMaxWidth(.5F),
                        )
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            maxLines = 1,
                            text = message.author.name,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = statusString,
                            maxLines = 1,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
                
                HorizontallyScrollableTagList(message.tags)
            }
            
            HorizontalDivider(Modifier.height(1.dp).fillMaxWidth().padding(8.dp))
            
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = message.content,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp).fillMaxHeight(),
                    )
                }
                
                val shouldDisabledReactionBarBeDisplayed =
                    (message.reactedWith != null || selectedReaction != null) || isArchived
                val shouldBottomSheetBeDisplayed =
                    isSenderView && !loading && error == null && reactionsAndUsers.isNotEmpty()
                val shouldReactionBarBeDisplayed = !isSenderView && !loading && error == null
                
                when {
                    shouldBottomSheetBeDisplayed -> {
                        ModalBottomSheet(
                            onDismissRequest = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                reactionsAndUsers
                                    .forEach { (user, reaction) ->
                                        SentMsgBottomSheetRow(reaction.reaction!!, user.name)
                                    }
                            }
                        }
                    }
                    shouldDisabledReactionBarBeDisplayed -> DisabledBottomPanel(
                        reaction = message.reactedWith ?: selectedReaction.toString(),
                        modifier = Modifier.padding(top = top).align(Alignment.BottomCenter)
                    )
                    shouldReactionBarBeDisplayed -> RecipientReactionBottomPanel(
                        availableReactions = message.availableReactions,
                        modifier = Modifier.padding(top = top).align(Alignment.BottomCenter),
                        onIsReactionBarVisible = { top = if (!it) 80.dp else 4.dp },
                    ) {
                        selectedReaction = it
                        try {
                            scope.launch {
                                val interaction = InteractionInfoData(
                                    userIdentifier = userIdentifier,
                                    messageExtId = message.externalId,
                                    type = InteractionType.reaction,
                                    reaction = it
                                )
                                
                                sendInteraction(accessToken, interaction, client)
                            }
                        } catch (e: UnauthorizedException) {
                            error = e
                        }
                    }
                }
            }
        }
        if (error != null) {
            ErrorDialog(
                errorMessage = error!!.message!!,
                isUnauthed = error is UnauthorizedException,
                modifier = Modifier.align(Alignment.Center)
            )
            throw error!!
        }
        
        if (loading)
            Box(Modifier.fillMaxSize()) {
                LinearProgressIndicator(Modifier.align(Alignment.Center))
            }
    }
}