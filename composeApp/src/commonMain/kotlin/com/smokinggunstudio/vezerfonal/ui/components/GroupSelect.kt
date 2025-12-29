package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.GroupSelectionState

@Composable
fun GroupSelect(
    state: GroupSelectionState,
    onCancelClick: ClickEvent,
    onApplyClick: CallbackEvent<List<GroupData>>
) {
    GeneralSelectionDialog(state, onCancelClick, onApplyClick)
}