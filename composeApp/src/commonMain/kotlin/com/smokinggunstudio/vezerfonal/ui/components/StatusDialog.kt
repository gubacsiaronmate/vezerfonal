package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.data.MessageStatusData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.network.api.sendInteraction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import io.ktor.client.request.invoke
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.alert
import vezerfonal.composeapp.generated.resources.close

@Composable
fun StatusDialog(
    accessToken: String,
    messageExtId: String,
    userIdentifier: String,
    recipientIdentifier: String,
    statusChanges: MessageStatusData,
    modifier: Modifier = Modifier,
    onClose: Function
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    var error by remember { mutableStateOf<Throwable?>(null) }
    
    Box(Modifier.fillMaxSize()) {
        Dialog(modifier = modifier) {
            statusChanges.sentAt?.let { StatusRow(it, MessageStatus.sent) }
            statusChanges.receivedAt?.let { StatusRow(it, MessageStatus.received) }
            statusChanges.readAt?.let { StatusRow(it, MessageStatus.read) }
            
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(1 / 2F)
                    .align(Alignment.End)
            ) {
                TextButton(onClose) { Text(stringResource(Res.string.close)) }
                TextButton({
                    try {
                        scope.launch {
                            sendInteraction(
                                client = client,
                                accessToken = accessToken,
                                interaction = InteractionInfoData(
                                    userIdentifier = userIdentifier,
                                    messageExtId = messageExtId,
                                    type = InteractionType.nudge,
                                    recipientIdentifier = recipientIdentifier
                                )
                            )
                        }
                    } catch (e: Exception) {
                        error = e
                    }
                }) { Text(stringResource(Res.string.alert)) }
            }
        }
        
        if (error != null)
            ErrorDialog(error!!, Modifier.align(Alignment.Center))
    }
}