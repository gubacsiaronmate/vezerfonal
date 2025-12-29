package com.smokinggunstudio.vezerfonal.ui.state.controller

import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.ui.state.model.SelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel

class TagSelectionStateController(initial: TagSelectionStateModel) : SelectionStateController<TagData>(initial) {
    override fun snapshot(): SelectionStateModel<TagData> {
        return TagSelectionStateModel(allItems, visibleItems, selectedItems)
    }
}