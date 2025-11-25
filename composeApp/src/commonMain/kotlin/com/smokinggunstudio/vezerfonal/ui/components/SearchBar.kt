package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.ui.helpers.SuspendCallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.SearchBarState
import kotlinx.coroutines.launch

@Composable
fun SearchBar(
    state: SearchBarState,
    modifier: Modifier = Modifier,
    onClick: SuspendCallbackClickEvent<SearchBarState>
) {
    val scope = rememberCoroutineScope()
    
    TextField(
        value = state.query,
        onValueChange = state::updateQuery,
        modifier = modifier
            .fillMaxWidth(),
        label = { Text(text = "Search") },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { scope.launch { onClick(state) } }) {
                Icon(Icons.Filled.Search,
                    contentDescription = "Search")
            }
        }
    )
}
