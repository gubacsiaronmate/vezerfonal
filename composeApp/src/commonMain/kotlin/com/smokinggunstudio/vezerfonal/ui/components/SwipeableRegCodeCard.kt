package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.ui.helpers.Event

@Composable
fun SwipeableRegCodeCard(
    regCode: RegCodeData,
    onEdit: Event,
    onDelete: Event,
) {
    SwipeableActionItem(
        isEditable = false,
        onEdit = onEdit,
        onDelete = onDelete,
    ) {
        Icon(Icons.Outlined.Key, null)
        Spacer(Modifier.width(8.dp))
        Text(text = "${regCode.code} (${regCode.remainingUses}/${regCode.totalUses})")
    }
}