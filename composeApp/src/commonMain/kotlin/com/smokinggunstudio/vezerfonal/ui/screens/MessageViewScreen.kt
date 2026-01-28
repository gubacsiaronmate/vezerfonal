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
import com.smokinggunstudio.vezerfonal.data.MessageStatusData
import com.smokinggunstudio.vezerfonal.data.UserInteractionData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.log
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.helpers.unaryPlus
import com.smokinggunstudio.vezerfonal.network.api.getReactionsAndUsersByMessageExtId
import com.smokinggunstudio.vezerfonal.network.api.getStatusChangesForMessageByUserExtId
import com.smokinggunstudio.vezerfonal.network.api.sendInteraction
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent
import com.smokinggunstudio.vezerfonal.ui.helpers.asStr
import com.smokinggunstudio.vezerfonal.ui.helpers.changeStatus
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
    val client = LocalHttpClient.current
    var error by remember { mutableStateOf<Throwable?>(null) }
    
    val msg = messageStr.toDTO<MessageData>()
    val isStatusSent = remember { msg.status == MessageStatus.sent }
    val message =
        if (isSenderView) msg
        else if (!isStatusSent) msg
        else msg.changeStatus(MessageStatus.received)
    
    val selectedMessage: ExternalId = message.externalId
    
    if (isStatusSent) LaunchedEffect(Unit) {
        try {
            sendInteraction(
                client = client,
                accessToken = accessToken,
                interaction = InteractionInfoData(
                    userIdentifier = userIdentifier,
                    messageExtId = message.externalId,
                    type = InteractionType.status,
                    status = MessageStatus.received
                )
            )
        } catch (e: Exception) { error = e }
    }
    
    val scope = rememberCoroutineScope()
    var top by remember { mutableStateOf(80.dp) }
    var selectedReaction by remember { mutableStateOf<String?>(null) }
    var reactionsAndUsers by remember { mutableStateOf<List<UserInteractionData>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    
    val shouldDisabledReactionBarBeDisplayed =
        ((message.reactedWith != null || selectedReaction != null) || isArchived) && !isSenderView
    val shouldReactionBarBeDisplayed = !isSenderView && !loading && error == null
    
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberStandardBottomSheetState(
        initialValue =
            if (!isSenderView) SheetValue.Hidden
            else SheetValue.PartiallyExpanded,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState,
        snackbarHostState = snackbarHostState
    )
    
    var isStatusDialogOpen by remember { mutableStateOf(false) }
    var selectedUser: ExternalId by remember { mutableStateOf("") }
    var interactionByUser: MessageStatusData? by remember { mutableStateOf(null) }
    var interactionsByUser: List<MessageStatusData> by remember { mutableStateOf(emptyList()) }
    
    
    if (isSenderView) {
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
        
        if (selectedUser.isNotEmpty()) LaunchedEffect(selectedUser) {
            loading = true
            try {
                val localStatusData = interactionsByUser
                    .singleOrNull { it.userExtId == selectedUser }
                
                val statusData = localStatusData
                    ?: suspend suspend@{
                        val data = getStatusChangesForMessageByUserExtId(
                            client = client,
                            accessToken = accessToken,
                            userExtId = selectedUser,
                            messageExtId = selectedMessage
                        )
                        interactionsByUser += data
                        return@suspend data
                    }()
                
                interactionByUser = statusData
                isStatusDialogOpen = true
            } catch (e: Exception) {
                error = e
            } finally {
                loading = false
            }
        }
    }
    
    reactionsAndUsers.forEach(::log)
    
    val content: ComposableContent = {
    
    }
    
    val statusString = "${stringResource(Res.string.status)}: ${message.status.asStr}"
    
    log { "Status is:" + message.status }
    
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.surface),
        sheetContent = { if (isSenderView) Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            reactionsAndUsers.forEach { (user, interaction) ->
                SentMsgBottomSheetRow(interaction.reaction!!, user.name) {
                    selectedUser = user.externalId
                }
            }
        } }
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(top = 16.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.Top,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 8.dp)
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
                                .padding(horizontal = 8.dp)
                        
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
                        Spacer(Modifier.height(8.dp))
                        HorizontallyScrollableTagList(message.tags)
                    }
                }
                
                HorizontalDivider(Modifier.height(1.dp).fillMaxWidth().padding(bottom = 8.dp))
                
                Box(Modifier.fillMaxSize()) {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            text = message.content,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp).fillMaxHeight(),
                        )
                    }
                    
                    when {
                        shouldDisabledReactionBarBeDisplayed -> DisabledBottomPanel(
                            reaction = message.reactedWith ?: selectedReaction,
                            modifier = Modifier.padding(top = top).align(Alignment.BottomCenter)
                        )
                        
                        shouldReactionBarBeDisplayed -> RecipientReactionBottomPanel(
                            availableReactions = message.availableReactions,
                            modifier = Modifier.padding(top = top).align(Alignment.BottomCenter),
                            onIsReactionBarVisible = { top = if (!it) 80.dp else 4.dp },
                        ) { reaction ->
                            selectedReaction = reaction
                            try {
                                scope.launch {
                                    sendInteraction(
                                        client = client,
                                        accessToken = accessToken,
                                        interaction = InteractionInfoData(
                                            userIdentifier = userIdentifier,
                                            messageExtId = message.externalId,
                                            type = InteractionType.reaction,
                                            reaction = reaction
                                        )
                                    )
                                }
                            } catch (e: UnauthorizedException) {
                                error = e
                            }
                        }
                    }
                }
            }
            
            if (error != null)
                ErrorDialog(error!!, Modifier.align(Alignment.Center))
            
            if (isStatusDialogOpen && interactionByUser != null)
                StatusDialog(interactionByUser!!) {
                    isStatusDialogOpen = false
                }
            
            if (loading) Box(Modifier.fillMaxSize()) {
                LinearProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}