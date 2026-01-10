package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.Event
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel

@Composable
fun TagSelect(
    snapshot: TagSelectionStateModel,
    onCancelClick: Event,
    onApplyClick: CallbackEvent<List<TagData>>
) {
    GeneralSelectionDialog(snapshot, onCancelClick, onApplyClick)
}