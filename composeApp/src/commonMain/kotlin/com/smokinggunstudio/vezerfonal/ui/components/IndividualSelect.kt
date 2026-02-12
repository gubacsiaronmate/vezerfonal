package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.toUrlValidFormat
import com.smokinggunstudio.vezerfonal.ui.state.model.UserSelectionStateModel

@Composable
fun IndividualSelect(
    snapshot: UserSelectionStateModel,
    onCancelClick: Function,
    onApplyClick: CallbackFunction<List<UserData>>
) {
    GeneralSelectionDialog(
        snapshot = snapshot,
        onCancelClick = onCancelClick,
        onApplyClick = onApplyClick,
        prefixContent = { ProfilePicture(it.toUrlValidFormat(), 24.dp) }
    )
}