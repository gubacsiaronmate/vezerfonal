package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.UserSelectionState

@Composable
fun IndividualSelect(
    state: UserSelectionState,
    onCancelClick: ClickEvent,
    onApplyClick: CallbackEvent<List<UserData>>
) {
    GeneralSelectionScreen(state, onCancelClick, onApplyClick)
}