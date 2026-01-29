package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.ui.helpers.Function

@Composable
fun SentMsgBottomSheetRow(
    interaction: InteractionInfoData,
    username: String,
    onClick: Function
) {
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfilePicture(size = 24.dp)
            Spacer(Modifier.width(24.dp))
            Text(username)
        }
        interaction.let {
            when (it.type) {
                InteractionType.status ->
                    if (it.status == MessageStatus.received)
                        Icon(Icons.Filled.Check, null)
                InteractionType.reaction -> with(it.reaction!!) {
                    if (isNotEmpty()) Text(this)
                    else Icon(Icons.Filled.DoneAll, null)
                }
                else -> {}
            }
        }
    }
}