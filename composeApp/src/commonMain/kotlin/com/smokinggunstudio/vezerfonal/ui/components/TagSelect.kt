package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.browse_tags

@Composable
fun TagSelect(
    snapshot: TagSelectionStateModel,
    onCancelClick: Function,
    onApplyClick: CallbackFunction<List<TagData>>
) {
    GeneralSelectionDialog(
        snapshot = snapshot,
        title = stringResource(Res.string.browse_tags),
        onCancelClick = onCancelClick,
        onApplyClick = onApplyClick,
    )
}
