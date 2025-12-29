package com.smokinggunstudio.vezerfonal.ui.state.controller

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.state.model.SelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.UserSelectionStateModel

class UserSelectionStateController(initial: UserSelectionStateModel) : SelectionStateController<UserData>(initial) {
    override fun snapshot(): SelectionStateModel<UserData> {
        return UserSelectionStateModel(allItems, visibleItems, selectedItems)
    }
}