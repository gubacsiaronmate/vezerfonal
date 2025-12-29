package com.smokinggunstudio.vezerfonal.ui.state.controller

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.ui.state.model.GroupSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.SelectionStateModel

class GroupSelectionStateController(initial: GroupSelectionStateModel) : SelectionStateController<GroupData>(initial) {
    override fun snapshot(): SelectionStateModel<GroupData> {
        return GroupSelectionStateModel(allItems, visibleItems, selectedItems)
    }
}