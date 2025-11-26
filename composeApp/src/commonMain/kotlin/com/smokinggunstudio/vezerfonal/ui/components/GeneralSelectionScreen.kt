package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.DTO
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.helpers.SuspendCallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.SearchBarState
import com.smokinggunstudio.vezerfonal.ui.state.SelectionState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.applyStr
import vezerfonal.composeapp.generated.resources.cancel

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
internal inline fun <reified T : DTO> GeneralSelectionScreen(
    state: SelectionState<T>,
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
            onClick = object : SuspendCallbackClickEvent<SearchBarState> {
                override suspend fun invoke(returns: SearchBarState) {
                    val localSearch = state.allItems.filter { it.name.contains(returns.query) }
                    state.visibleItems = localSearch.ifEmpty {
                        state.allItems // TODO: Group get
                    }
                }
            }
        )
        Box{
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier
                        .width(160.dp)
                        .height(48.dp),
                    onClick = {},
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
                    onClick = {},
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