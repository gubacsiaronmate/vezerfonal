package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.NamedDTO
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.SuspendCallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.SearchBarState
import com.smokinggunstudio.vezerfonal.ui.state.SelectionState
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.applyStr
import vezerfonal.composeapp.generated.resources.cancel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal inline fun <reified T : NamedDTO> GeneralSelectionDialog(
    state: SelectionState<T>,
    noinline onCancelClick: ClickEvent,
    onApplyClick: CallbackEvent<List<T>>
) {
    val searchBarState by remember { mutableStateOf(SearchBarState()) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        SearchBar(
            state = searchBarState,
            onClick = SuspendCallbackClickEvent { query ->
                val localSearch = state.allItems.filter { it.name.contains(query.query) }
                state.visibleItems = localSearch.ifEmpty {
                    state.allItems // TODO: Group get
                }
            }
        )
        Box(modifier = Modifier.weight(1F)){
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize()) {
                state.visibleItems.forEach {
                    key(it.name) {
                        SelectionListItem(
                            item = it,
                            isChecked = it in state.selectedItems,
                            prefixContent = { if (it is UserData) ProfilePicture(size = 24.dp) }
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
                    modifier = Modifier
                        .width(160.dp),
                    onClick = { onApplyClick(state.selectedItems); onCancelClick() },
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