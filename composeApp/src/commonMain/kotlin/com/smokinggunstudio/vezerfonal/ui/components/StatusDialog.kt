package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.data.MessageStatusData
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.alert
import vezerfonal.composeapp.generated.resources.close

@Composable
fun StatusDialog(
    statusChanges: MessageStatusData,
    modifier: Modifier = Modifier,
    onClose: Function
) {
    Dialog(modifier = modifier) {
        statusChanges.sentAt?.let { StatusRow(it, MessageStatus.sent) }
        statusChanges.receivedAt?.let { StatusRow(it, MessageStatus.received) }
        statusChanges.readAt?.let { StatusRow(it, MessageStatus.read) }
        
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(1/2F)
                .align(Alignment.End)
        ) {
            TextButton(onClose) { Text(stringResource(Res.string.close)) }
            TextButton({
//                TODO: nudge/alert system
            }) { Text(stringResource(Res.string.alert)) }
        }
    }
}