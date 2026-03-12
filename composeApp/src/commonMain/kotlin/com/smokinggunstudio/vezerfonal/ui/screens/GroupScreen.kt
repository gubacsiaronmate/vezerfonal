package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.Identifier
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.getAllUsers
import com.smokinggunstudio.vezerfonal.ui.components.CreateGroupDialog
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.GroupCard
import com.smokinggunstudio.vezerfonal.ui.components.JoinGroupDialog
import com.smokinggunstudio.vezerfonal.ui.components.SwipeableGroupCard
import com.smokinggunstudio.vezerfonal.ui.helpers.ContentContainer
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create_group
import vezerfonal.composeapp.generated.resources.groups
import vezerfonal.composeapp.generated.resources.join_group

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun GroupScreen(
    accessToken: String,
    myIdentifier: Identifier,
    groupData: List<GroupData>,
    isSuperAdminLogIn: Boolean = false
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    val widthClass = LocalWindowSizeInfo.current.widthClass
    var groups by remember(groupData) { mutableStateOf(groupData) }
    var isCreatePopUpOn by remember { mutableStateOf(false) }
    var isJoinPopUpOn by remember { mutableStateOf(false) }
    var users by remember { mutableStateOf<List<UserData>?>(null) }
    var loaded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<Throwable?>(null) }

    if (isSuperAdminLogIn) LaunchedEffect(Unit) {
        val d = try {
            getAllUsers(accessToken, client)
        } catch (e: UnauthorizedException) {
            error = e
            return@LaunchedEffect
        }
        users = d
        loaded = true
    } else loaded = true

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.groups),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    TextButton(
                        onClick = { isJoinPopUpOn = true },
                        modifier = Modifier.padding(end = Spacing.sm),
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(Modifier.width(Spacing.xs))
                        Text(stringResource(Res.string.join_group))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
            )
        },
        floatingActionButton = {
            if (isSuperAdminLogIn) {
                if (widthClass == WindowWidthClass.Expanded) {
                    ExtendedFloatingActionButton(
                        onClick = { isCreatePopUpOn = true },
                        icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                        text = { Text(stringResource(Res.string.create_group)) },
                    )
                } else {
                    FloatingActionButton(onClick = { isCreatePopUpOn = true }) {
                        Icon(Icons.Outlined.Add, contentDescription = null)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            ContentContainer {
                if (widthClass != WindowWidthClass.Compact) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(Spacing.md),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                        verticalArrangement = Arrangement.spacedBy(Spacing.md),
                    ) {
                        items(groups) { group ->
                            if (isSuperAdminLogIn)
                                SwipeableGroupCard(
                                    onEdit = { },
                                    onDelete = {
                                        groups = groups.filter { it != group }
                                        scope.launch { /* TODO: delete group */ }
                                    },
                                    group = group,
                                    myIdentifier = myIdentifier,
                                )
                            else GroupCard(
                                name = group.name,
                                extId = group.externalId,
                                description = group.description,
                                amITheAdmin = group.adminIdentifier == myIdentifier,
                            )
                        }
                    }
                } else {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(groups) { group ->
                            if (isSuperAdminLogIn)
                                SwipeableGroupCard(
                                    onEdit = { },
                                    onDelete = {
                                        groups = groups.filter { it != group }
                                        scope.launch { /* TODO: delete group */ }
                                    },
                                    group = group,
                                    myIdentifier = myIdentifier,
                                )
                            else GroupCard(
                                name = group.name,
                                extId = group.externalId,
                                description = group.description,
                                amITheAdmin = group.adminIdentifier == myIdentifier,
                            )
                        }
                    }
                }
            }

            if (isSuperAdminLogIn && isCreatePopUpOn && loaded)
                CreateGroupDialog(
                    accessToken = accessToken,
                    users = users!!,
                    modifier = Modifier.align(Alignment.Center),
                    onCancelClick = { isCreatePopUpOn = false },
                ) { groups += it }

            if (isJoinPopUpOn) JoinGroupDialog(
                accessToken = accessToken,
                modifier = Modifier.align(Alignment.Center),
                onCancelClick = { isJoinPopUpOn = false },
            ) { groups += it }

            if (error != null) ErrorDialog(error!!, Modifier.align(Alignment.Center))
        }
    }
}
