package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.network.api.tagDelete
import com.smokinggunstudio.vezerfonal.network.api.tagPut
import com.smokinggunstudio.vezerfonal.ui.components.CreateTagDialog
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.SwipeableTagCard
import com.smokinggunstudio.vezerfonal.ui.components.TagEditDialog
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create_tag
import vezerfonal.composeapp.generated.resources.search
import vezerfonal.composeapp.generated.resources.tag_management

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagManagementScreen(
    accessToken: String,
    tagsList: List<TagData>,
    onBack: () -> Unit,
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    val isExpanded = LocalWindowSizeInfo.current.widthClass == WindowWidthClass.Expanded
    var tags by remember(tagsList) { mutableStateOf(tagsList) }
    var isCreateTagOpened by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf("") }
    var isTagEditOpened by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val filteredTags = remember(tags, searchQuery) {
        if (searchQuery.isBlank()) tags
        else tags.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.tag_management)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isSearchVisible = !isSearchVisible
                        if (!isSearchVisible) searchQuery = ""
                    }) {
                        Icon(
                            imageVector = if (isSearchVisible) Icons.Filled.Close else Icons.Outlined.Search,
                            contentDescription = null,
                        )
                    }
                    Box {
                        IconButton(onClick = { isMenuExpanded = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(Res.string.create_tag)) },
                                leadingIcon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                                onClick = { isMenuExpanded = false; isCreateTagOpened = true },
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(Modifier.fillMaxSize()) {
                AnimatedVisibility(isSearchVisible) {
                    Column {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text(stringResource(Res.string.search)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
                            shape = MaterialTheme.shapes.extraLarge,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            ),
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                }

                val screenContent: @Composable ColumnScope.() -> Unit = {
                    filteredTags.forEach { tag ->
                        SwipeableTagCard(
                            onDelete = {
                                tags = tags.filter { it != tag }
                                scope.launch { tagDelete(client, accessToken, tag) }
                            },
                            onEdit = {
                                selectedTag = tag.name
                                isTagEditOpened = true
                            },
                            tag = tag
                        )
                    }
                    Spacer(Modifier.height(Spacing.xl))
                }

                if (isExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.xl),
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        Card(
                            modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth(),
                            shape = MaterialTheme.shapes.extraLarge,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Spacing.lg)
                                    .verticalScroll(rememberScrollState()),
                            ) { screenContent() }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.lg)
                            .verticalScroll(rememberScrollState()),
                    ) { screenContent() }
                }
            }

            if (isCreateTagOpened)
                CreateTagDialog(
                    accessToken = accessToken,
                    onCancelClick = { isCreateTagOpened = false }
                ) { tags += it }

            if (isTagEditOpened && selectedTag.isNotBlank())
                TagEditDialog(
                    modifier = Modifier.align(Alignment.Center),
                    onCancelClick = { isTagEditOpened = false }
                ) { newTagName ->
                    val newTag = TagData(newTagName)
                    tags = tags.map { if (it.name != selectedTag) it else newTag }
                    scope.launch {
                        try {
                            tagPut(client, accessToken, newTag)
                        } catch (e: Exception) {
                            error = e
                        }
                    }
                }

            if (error != null) ErrorDialog(error!!, Modifier.align(Alignment.Center))
        }
    }
}
