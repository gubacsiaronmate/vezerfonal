package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.ui.state.GroupSelectionState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun GroupSelect(
    state: GroupSelectionState,
) {
    GeneralSelectionScreen(state)
}