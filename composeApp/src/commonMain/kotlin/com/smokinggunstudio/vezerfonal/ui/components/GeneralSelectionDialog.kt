package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.NamedDTO
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.Event
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent
import com.smokinggunstudio.vezerfonal.ui.helpers.SuspendCallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.controller.GroupSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.SearchBarStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.SelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.TagSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.UserSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.GroupSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.SearchBarStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.SelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.UserSelectionStateModel
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.applyStr
import vezerfonal.composeapp.generated.resources.cancel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal inline fun <reified T : NamedDTO> GeneralSelectionDialog(
    snapshot: SelectionStateModel<T>,
    noinline onCancelClick: Event,
    onApplyClick: CallbackEvent<List<T>>,
    noinline prefixContent: ComposableContent = { },
) {
    @Suppress("UNCHECKED_CAST")
    val state: SelectionStateController<T> = remember {
        when(snapshot) {
            is UserSelectionStateModel -> UserSelectionStateController(snapshot)
            is GroupSelectionStateModel -> GroupSelectionStateController(snapshot)
            is TagSelectionStateModel -> TagSelectionStateController(snapshot)
            else -> error("Invalid snapshot")
        } as SelectionStateController<T>
    }
    val searchBarState = remember { SearchBarStateController(SearchBarStateModel()) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .onVisibilityChanged { visible -> if (!visible) onCancelClick() }
    ) {
        SearchBar(
            snapshot = searchBarState.snapshot(),
            onClick = SuspendCallbackClickEvent { state.search(it.query) }
        )
        Box(modifier = Modifier.weight(1F)){
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize()) {
                state.visibleItems.forEach {
                    key(it.externalId) {
                        SelectionListItem(
                            item = it,
                            isChecked = it in state.selectedItems,
                            prefixContent = prefixContent
                        ) { checked -> if (checked) state.addItem(it) else state.removeItem(it) }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.BottomCenter)
                    .background(color = MaterialTheme.colorScheme.surface),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    modifier = Modifier
                        .width(160.dp),
                    onClick = onCancelClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = stringResource(Res.string.cancel),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        onApplyClick(state.selectedItems.toList())
                        onCancelClick()
                    },
                ) {
                    Text(
                        text = stringResource(Res.string.applyStr),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}