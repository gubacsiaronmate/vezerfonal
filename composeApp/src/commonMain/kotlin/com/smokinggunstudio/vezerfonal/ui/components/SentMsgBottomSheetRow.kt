package com.smokinggunstudio.vezerfonal.ui.components

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

@Composable
fun SentMsgBottomSheetRow(
    reaction: String,
    username: String
) {
    
    Row(
        modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
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
        reaction.let {
            if (it.isNotEmpty()) Text(it)
            else Icon(Icons.Filled.DoneAll, null)
        }
    }
}