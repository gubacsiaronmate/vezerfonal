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
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.SearchBarState
import vezerfonal.composeapp.generated.resources.Res

@Composable
fun SimpleSearchBar(
    modifier: Modifier = Modifier,
    onClick: CallbackClickEvent<SearchBarState>
) {
    val state = remember { SearchBarState() }
    
    TextField(
        value = state.query,
        onValueChange = state::updateQuery,
        modifier = modifier
            .fillMaxWidth(),
        label = { Text(text = "Search") },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { onClick(state) }) {
                Icon(Icons.Filled.Search,
                    contentDescription = "Search")
            }
        }
    )
}
