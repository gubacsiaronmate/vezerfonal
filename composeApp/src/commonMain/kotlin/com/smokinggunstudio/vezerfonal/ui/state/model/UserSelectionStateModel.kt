package com.smokinggunstudio.vezerfonal.ui.state.model

import com.smokinggunstudio.vezerfonal.data.UserData

data class UserSelectionStateModel(
    override val allItems: List<UserData> = emptyList(),
    override val visibleItems: List<UserData> = emptyList(),
    override val selectedItems: Set<UserData> = emptySet(),
) : SelectionStateModel<UserData>
