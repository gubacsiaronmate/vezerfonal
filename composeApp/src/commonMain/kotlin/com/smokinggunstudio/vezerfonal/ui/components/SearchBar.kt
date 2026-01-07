package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.ui.helpers.SuspendCallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.controller.SearchBarStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.SearchBarStateModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.search

@Composable
fun SearchBar(
    snapshot: SearchBarStateModel,
    modifier: Modifier = Modifier,
    onClick: SuspendCallbackClickEvent<SearchBarStateModel>
) {
    val scope = rememberCoroutineScope()
    val state = remember { SearchBarStateController(snapshot) }
    
    TextField(
        value = state.query,
        onValueChange = state::updateQuery,
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = stringResource(Res.string.search)) },
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        onClick(state.snapshot())
                    }
                }
            ) { Icon(Icons.Filled.Search, stringResource(Res.string.search)) }
        }
    )
}
