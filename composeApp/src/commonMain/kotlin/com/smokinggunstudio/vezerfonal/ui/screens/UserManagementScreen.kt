package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.network.api.approveDeletion
import com.smokinggunstudio.vezerfonal.network.api.denyDeletion
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    users: List<UserData>,
    accessToken: String,
    client: HttpClient,
    onBack: () -> Unit,
) {
    val isExpanded = LocalWindowSizeInfo.current.widthClass == WindowWidthClass.Expanded
    var userList by remember { mutableStateOf(users) }
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredUsers = remember(userList, searchQuery) {
        if (searchQuery.isBlank()) userList
        else userList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.email.contains(searchQuery, ignoreCase = true)
        }
    }
    val pendingDeletion = filteredUsers.filter { it.deletionRequested }
    val activeUsers = filteredUsers.filter { !it.deletionRequested }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.user_management)) },
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
                    if (pendingDeletion.isNotEmpty()) {
                        Spacer(Modifier.height(Spacing.sm))
                        Text(
                            text = stringResource(Res.string.deletion_requests),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = Spacing.xs),
                        )
                        pendingDeletion.forEach { user ->
                            DeletionRequestRow(
                                user = user,
                                accessToken = accessToken,
                                client = client,
                                onActionCompleted = {
                                    userList = userList.map { u ->
                                        if (u.externalId == user.externalId) u.copy(deletionRequested = false) else u
                                    }
                                },
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.md))
                    }
                    activeUsers.forEach { user -> UserRow(user) }
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
        }
    }
}

@Composable
private fun DeletionRequestRow(
    user: UserData,
    accessToken: String,
    client: HttpClient,
    onActionCompleted: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg, vertical = Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f),
                )
            }
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                )
            } else {
                TextButton(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            try {
                                denyDeletion(client, accessToken, user.externalId)
                                onActionCompleted()
                            } finally { isLoading = false }
                        }
                    }
                ) { Text(stringResource(Res.string.deny_deletion), color = MaterialTheme.colorScheme.onErrorContainer) }
                Spacer(Modifier.width(Spacing.xs))
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            try {
                                approveDeletion(client, accessToken, user.externalId)
                                onActionCompleted()
                            } finally { isLoading = false }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                ) { Text(stringResource(Res.string.approve_deletion)) }
            }
        }
    }
}

@Composable
private fun UserRow(user: UserData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg, vertical = Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (user.isSuperAdmin) {
                AdminBadge(stringResource(Res.string.super_admin), MaterialTheme.colorScheme.error)
            } else if (user.isAnyAdmin) {
                AdminBadge(stringResource(Res.string.admin), MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun AdminBadge(label: String, color: androidx.compose.ui.graphics.Color) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.15f),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs),
        )
    }
}
