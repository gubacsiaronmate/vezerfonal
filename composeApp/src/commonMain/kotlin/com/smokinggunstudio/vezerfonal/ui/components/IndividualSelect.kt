package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.UserSelectionState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun IndividualSelect(
    state: UserSelectionState,
    onCancelClick: ClickEvent
) {
    GeneralSelectionScreen(state, onCancelClick)
}