package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.now
import com.smokinggunstudio.vezerfonal.network.api.sendInteraction
import com.smokinggunstudio.vezerfonal.ui.components.DisabledBottomPanel
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.HorizontallyScrollableTagList
import com.smokinggunstudio.vezerfonal.ui.components.RecipientReactionBottomPanel
import com.smokinggunstudio.vezerfonal.ui.helpers.capitalize
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.status

@Composable
fun MessageViewScreen(
    client: HttpClient,
    accessToken: String,
    message: MessageData
) {
    val scope = rememberCoroutineScope()
    var top by remember { mutableStateOf(80.dp) }
    val statusAsStr = (message.status ?: MessageStatus.received).toString().capitalize()
    val statusString = "${stringResource(Res.string.status)}: $statusAsStr"
    var selectedReaction by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<Throwable?>(null)}
    
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(color = MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
                
                if (message.reactedWith != null || selectedReaction != null)
                    DisabledBottomPanel(
                        reaction = message.reactedWith ?: selectedReaction!!,
                        modifier = Modifier.padding(top = top).align(Alignment.BottomCenter)
                    )
                else RecipientReactionBottomPanel(
                    availableReactions = message.availableReactions,
                    modifier = Modifier.padding(top = top).align(Alignment.BottomCenter),
                    onIsReactionBarVisible = { top = if (!it) 80.dp else 4.dp },
                ) {
                    selectedReaction = it
                    try {
                        scope.launch {
                            val interaction = InteractionInfoData(
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
        if (error != null) ErrorDialog(error!!.message!!, true)
    }
}