package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.helpers.Identifier
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SwipeableGroupCard(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    group: GroupData,
    myIdentifier: Identifier,
) {
    SwipeableActionItem(
        isEditable = true,
        onEdit = onEdit,
        onDelete = onDelete
    ) { GroupCard(
        name = group.name,
        extId = group.externalId,
        description = group.description,
        amITheAdmin = group.adminIdentifier == myIdentifier
    ) }
}