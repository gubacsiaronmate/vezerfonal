package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.Event
import com.smokinggunstudio.vezerfonal.ui.state.model.UserSelectionStateModel

@Composable
fun IndividualSelect(
    snapshot: UserSelectionStateModel,
    onCancelClick: Event,
    onApplyClick: CallbackEvent<List<UserData>>
) {
    GeneralSelectionDialog(snapshot, onCancelClick, onApplyClick, prefixContent = { ProfilePicture(size = 24.dp) })
}