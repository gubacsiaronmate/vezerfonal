package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.TagSelectionState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TagSelect(
    state: TagSelectionState,
    onCancelClick: ClickEvent,
    onApplyClick: CallbackEvent<List<TagData>>
) {
    GeneralSelectionScreen(state, onCancelClick, onApplyClick)
}