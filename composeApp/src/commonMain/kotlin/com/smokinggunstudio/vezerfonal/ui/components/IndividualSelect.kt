package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.smokinggunstudio.vezerfonal.ui.state.UserSelectionState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun IndividualSelect() {
    val state = remember { UserSelectionState() }
    
    GeneralSelectionScreen(state)
}