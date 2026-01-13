package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel

@Composable
fun TagSelect(
    snapshot: TagSelectionStateModel,
    onCancelClick: Function,
    onApplyClick: CallbackFunction<List<TagData>>
) {
    GeneralSelectionDialog(snapshot, onCancelClick, onApplyClick)
}