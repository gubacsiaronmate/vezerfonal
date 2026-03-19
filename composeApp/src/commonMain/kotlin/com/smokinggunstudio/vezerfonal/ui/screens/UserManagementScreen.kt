package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@Composable
fun UserManagementScreen(
    users: List<UserData>,
    accessToken: String,
    client: HttpClient,
) {
    val isExpanded = LocalWindowSizeInfo.current.widthClass == WindowWidthClass.Expanded

    var userList by remember { mutableStateOf(users) }
    val pendingDeletion = userList.filter { it.deletionRequested }
    val activeUsers = userList.filter { !it.deletionRequested }

    val screenContent: @Composable ColumnScope.() -> Unit = {
        Text(
            text = stringResource(Res.string.user_management),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = Spacing.sm),
        )

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
                    onActionCompleted = { userList = userList.map { u ->
                        if (u.externalId == user.externalId) u.copy(deletionRequested = false) else u
                    }},
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
