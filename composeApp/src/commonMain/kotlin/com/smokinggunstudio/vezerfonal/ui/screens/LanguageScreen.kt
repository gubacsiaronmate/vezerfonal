package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.SettingRow
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun LanguageScreen(
    selectedTag: String?,
    onLanguageSelected: (String) -> Unit,
) {
    val options = listOf(
        "en" to stringResource(Res.string.english),
        "hu" to stringResource(Res.string.hungarian),
    )
    val isExpanded = LocalWindowSizeInfo.current.widthClass == WindowWidthClass.Expanded

    val screenContent: @Composable ColumnScope.() -> Unit = {
        Spacer(Modifier.height(Spacing.lg))
        Text(
            text = stringResource(Res.string.languages),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.xs),
        )
        options.forEach { (tag, name) ->
            SettingRow(
                imageVector = Icons.Outlined.Language,
                text = name,
                trailing = @Composable {
                    if (selectedTag == tag) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                onClick = { onLanguageSelected(tag) },
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
