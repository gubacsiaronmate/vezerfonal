package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.ui.components.SettingRow
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun ArchiveOptionsScreen(
    isEnabled: Boolean,
    minStatus: MessageStatus,
    delayHours: Int,
    onEnabledChange: CallbackFunction<Boolean>,
    onMinStatusChange: CallbackFunction<MessageStatus>,
    onDelayChange: CallbackFunction<Int>,
) {
    val statusOptions = listOf(
        MessageStatus.sent to stringResource(Res.string.sent),
        MessageStatus.received to stringResource(Res.string.received),
        MessageStatus.read to stringResource(Res.string.read),
    )
    val delayOptions = listOf(
        24 to stringResource(Res.string.one_day),
        72 to stringResource(Res.string.three_days),
        168 to stringResource(Res.string.one_week),
        336 to stringResource(Res.string.two_weeks),
        720 to stringResource(Res.string.one_month),
    )
    val isExpanded = LocalWindowSizeInfo.current.widthClass == WindowWidthClass.Expanded

    val screenContent: @Composable ColumnScope.() -> Unit = {
        Spacer(Modifier.height(Spacing.lg))
        Text(
            text = stringResource(Res.string.auto_archive),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.xs),
        )
        SettingRow(
            imageVector = Icons.Outlined.Archive,
            text = stringResource(Res.string.auto_archive),
            trailing = @Composable {
                Switch(
                    checked = isEnabled,
                    onCheckedChange = { onEnabledChange(it) },
                )
            },
        )

        if (isEnabled) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = Spacing.lg))

            Spacer(Modifier.height(Spacing.lg))
            Text(
                text = stringResource(Res.string.minimum_read_state),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.xs),
            )
            statusOptions.forEach { (status, label) ->
                SettingRow(
                    imageVector = Icons.Outlined.MarkEmailRead,
                    text = label,
                    trailing = @Composable {
                        if (minStatus == status) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                    onClick = { onMinStatusChange(status) },
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = Spacing.lg))

            Spacer(Modifier.height(Spacing.lg))
            Text(
                text = stringResource(Res.string.archive_after),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.xs),
            )
            delayOptions.forEach { (hours, label) ->
                SettingRow(
                    imageVector = Icons.Outlined.Timer,
                    text = label,
                    trailing = @Composable {
                        if (delayHours == hours) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                    onClick = { onDelayChange(hours) },
                )
            }
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
                        .verticalScroll(rememberScrollState()),
                ) { screenContent() }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) { screenContent() }
    }
}
