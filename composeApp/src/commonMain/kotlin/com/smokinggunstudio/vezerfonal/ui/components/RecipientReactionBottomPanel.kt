package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddReaction
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.ShapeModifier

@Composable
fun RecipientReactionBottomPanel(
    availableReactions: List<String>?,
    modifier: Modifier = Modifier,
    onIsReactionBarVisible: CallbackFunction<Boolean>,
    onReactionSelected: CallbackFunction<String>
) {
    var isReactionBarVisible by remember { mutableStateOf(false) }
    var selectedReaction by remember { mutableStateOf<String?>(null) }
    
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = ShapeModifier.TOP_ROUNDED.toShape()
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = ShapeModifier.TOP_ROUNDED.toShape()
            ),
        verticalArrangement = Arrangement.Bottom,
    ) {
        if (isReactionBarVisible)
            availableReactions
                ?.let { reactions ->
                    RecipientReactionBar(reactions) {
                        selectedReaction = it
                        isReactionBarVisible = false
                    }
                }
        Row {
            if (!availableReactions.isNullOrEmpty()) IconButton(
                onClick = { isReactionBarVisible = !isReactionBarVisible; onIsReactionBarVisible(isReactionBarVisible) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                if (selectedReaction == null)
                    Icon(
                        imageVector = Icons.Outlined.AddReaction,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                else
                    Text(
                        text = selectedReaction!!,
                    )
            }
            SlidingConfirmButton(8.dp, Modifier.height(64.dp)) {
                isReactionBarVisible = false
                onReactionSelected(selectedReaction ?: "")
            }
        }
    }
}