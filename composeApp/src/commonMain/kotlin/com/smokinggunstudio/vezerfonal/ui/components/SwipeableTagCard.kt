package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.TagData

@Composable
fun SwipeableTagCard(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    tag: TagData
) {
    SwipeableActionItem(
        isEditable = true,
        onEdit = onEdit,
        onDelete = onDelete
    ) { TagCard(tag.name) }
}