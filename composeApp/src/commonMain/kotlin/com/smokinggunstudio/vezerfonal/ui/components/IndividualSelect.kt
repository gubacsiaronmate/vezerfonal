package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.toUrlValidFormat
import com.smokinggunstudio.vezerfonal.ui.state.model.UserSelectionStateModel
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.individuals

@Composable
fun IndividualSelect(
    snapshot: UserSelectionStateModel,
    onCancelClick: Function,
    onApplyClick: CallbackFunction<List<UserData>>
) {
    GeneralSelectionDialog(
        snapshot = snapshot,
        title = stringResource(Res.string.individuals),
        onCancelClick = onCancelClick,
        onApplyClick = onApplyClick,
        prefixContent = { user ->
            ProfilePicture(
                name = user.name.toUrlValidFormat(),
                size = 48.dp,
                profilePicFilename = user.profilePicFilename,
            )
        },
    )
}
