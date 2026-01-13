package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.state.model.GroupSelectionStateModel

@Composable
fun GroupSelect(
    snapshot: GroupSelectionStateModel,
    onCancelClick: Function,
    onApplyClick: CallbackFunction<List<GroupData>>
) {
    GeneralSelectionDialog(snapshot, onCancelClick, onApplyClick)
}