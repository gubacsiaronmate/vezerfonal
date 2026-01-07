package com.smokinggunstudio.vezerfonal.ui.state.model

import com.smokinggunstudio.vezerfonal.data.TagData
import kotlinx.serialization.Serializable

@Serializable
data class TagSelectionStateModel(
    override val allItems: List<TagData> = emptyList(),
    override val visibleItems: List<TagData> = emptyList(),
    override val selectedItems: Set<TagData> = emptySet()
) : SelectionStateModel<TagData>
