package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onVisibilityChanged
import com.smokinggunstudio.vezerfonal.data.NamedDTO
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.state.controller.GroupSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.SelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.TagSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.UserSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.GroupSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.SelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.UserSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.applyStr
import vezerfonal.composeapp.generated.resources.search

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal inline fun <reified T : NamedDTO> GeneralSelectionDialog(
    snapshot: SelectionStateModel<T>,
    title: String,
    noinline onCancelClick: Function,
    onApplyClick: CallbackFunction<List<T>>,
    noinline prefixContent: (@Composable (String) -> Unit)? = null,
) {
    @Suppress("UNCHECKED_CAST")
    val state: SelectionStateController<T> = remember {
        when (snapshot) {
            is UserSelectionStateModel -> UserSelectionStateController(snapshot)
            is GroupSelectionStateModel -> GroupSelectionStateController(snapshot)
            is TagSelectionStateModel -> TagSelectionStateController(snapshot)
            else -> error("Invalid snapshot")
        } as SelectionStateController<T>
    }
    var query by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.onVisibilityChanged { visible -> if (!visible) onCancelClick() },
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onCancelClick) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                },
                title = { Text(title) },
                actions = {
                    TextButton(onClick = {
                        onApplyClick(state.selectedItems.toList())
                        onCancelClick()
                    }) {
                        Text(stringResource(Res.string.applyStr))
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it; state.search(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
                placeholder = { Text(stringResource(Res.string.search)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.extraLarge,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                ),
            )
            state.visibleItems.forEach { item ->
                key(item.externalId) {
                    SelectionListItem(
                        item = item,
                        isChecked = item in state.selectedItems,
                        prefixContent = if (prefixContent != null) { { prefixContent(item.name) } } else null,
                    ) { checked -> if (checked) state.addItem(item) else state.removeItem(item) }
                }
            }
        }
    }
}
