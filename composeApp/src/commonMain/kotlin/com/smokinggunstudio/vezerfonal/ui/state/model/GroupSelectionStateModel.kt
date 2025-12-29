package com.smokinggunstudio.vezerfonal.ui.state.model

import com.smokinggunstudio.vezerfonal.data.GroupData

data class GroupSelectionStateModel(
    override val allItems: List<GroupData> = emptyList(),
    override val visibleItems: List<GroupData> = emptyList(),
    override val selectedItems: Set<GroupData> = emptySet(),
) : SelectionStateModel<GroupData>
