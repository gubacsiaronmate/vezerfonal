package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.model.GroupSelectionStateModel

@Composable
fun GroupSelect(
    snapshot: GroupSelectionStateModel,
    onCancelClick: ClickEvent,
    onApplyClick: CallbackEvent<List<GroupData>>
) {
    GeneralSelectionDialog(snapshot, onCancelClick, onApplyClick)
}