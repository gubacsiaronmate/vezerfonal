package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SwipeableRegCodeCard(
    onDelete: () -> Unit,
    regCode: RegCodeData
) {
    SwipeableActionItem(
        isEditable = false,
        onDelete = onDelete,
    ) {
        Icon(
            imageVector = Icons.Outlined.Key,
            contentDescription = null
        )
        Text(text = regCode.code)
        Text(text = "${regCode.remainingUses ?: 0}/${regCode.totalUses}")
    }
}