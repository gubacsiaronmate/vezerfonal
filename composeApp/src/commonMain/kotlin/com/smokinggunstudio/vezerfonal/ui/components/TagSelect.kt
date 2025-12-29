package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.TagSelectionState

@Composable
fun TagSelect(
    state: TagSelectionState,
    onCancelClick: ClickEvent,
    onApplyClick: CallbackEvent<List<TagData>>
) {
    GeneralSelectionDialog(state, onCancelClick, onApplyClick)
}