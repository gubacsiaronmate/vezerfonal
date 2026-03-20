package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.sendMessage
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.helpers.limit
import com.smokinggunstudio.vezerfonal.ui.state.controller.GroupSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.TagSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.UserSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.WriteMessageStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.GroupSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.UserSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.WriteMessageStateModel
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun WriteMessageScreen(
    user: UserData,
    accessToken: String,
    guiao: List<GroupData>,
    userList: List<UserData>,
    tagList: List<TagData>
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    val state = remember { WriteMessageStateController(WriteMessageStateModel()) }
    var isGroupTabOpened by remember { mutableStateOf(false) }
    var isIndividualTabOpened by remember { mutableStateOf(false) }
    var isTagSelectTabOpened by remember { mutableStateOf(false) }
    val groupSelectionState = remember { GroupSelectionStateController(GroupSelectionStateModel()) }
    val userSelectionState = remember { UserSelectionStateController(UserSelectionStateModel()) }
    val tagSelectionState = remember { TagSelectionStateController(TagSelectionStateModel()) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    val isExpanded = LocalWindowSizeInfo.current.widthClass == WindowWidthClass.Expanded

    groupSelectionState.loadAllItems(guiao)
    userSelectionState.loadAllItems(userList)
    tagSelectionState.loadAllItems(tagList)

    // Composable for the recipients/urgency panel
    val recipientsPanel: @Composable ColumnScope.() -> Unit = {
        Text(
            text = stringResource(Res.string.to),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(Spacing.xl))
        Row(
            modifier = Modifier.fillMaxWidth().padding(Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                RecipientSelectButton(
                    text = stringResource(Res.string.groups),
                    selectedAmount = state.groups.size,
                    onClick = { isGroupTabOpened = true },
                )
                RecipientSelectButton(
                    text = stringResource(Res.string.individuals),
                    selectedAmount = state.userIdentifiers.size,
                    onClick = { isIndividualTabOpened = true },
                )
            }
            FilterChip(
                selected = state.isUrgent,
                onClick = { state.updateUrgency(!state.isUrgent) },
                label = { Text(stringResource(Res.string.urgent)) },
                leadingIcon = {
                    Icon(
                        imageVector = if (state.isUrgent) Icons.Filled.Error else Icons.Outlined.ErrorOutline,
                        contentDescription = null,
                        tint = if (state.isUrgent) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )
        }
    }

    // Composable for the compose/message panel
    val composePanel: @Composable ColumnScope.() -> Unit = {
        OutlinedTextField(
            value = state.title,
            onValueChange = state::updateTitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
            label = {
                Text(
                    text = stringResource(Res.string.subject),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            singleLine = true,
        )
        OutlinedTextField(
            value = state.content,
            onValueChange = state::updateContent,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
            label = {
                Text(
                    text = stringResource(Res.string.message),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.md)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
        ) {
            ReactionBar(
                buttonEmojis = state.availableReactions,
                onSetEmoji = { i, emoji -> state.addReaction(emoji, i) },
                onClearEmoji = { i -> state.removeReaction(i) },
            )
            Spacer(Modifier.height(Spacing.md))
            HorizontallyScrollableTagSelect(
                tagList = tagSelectionState.allItems.map { it.name }.limit(5),
                tabOpenedCallback = { isTagSelectTabOpened = true },
            ) { (checked, tag) ->
                with(tagSelectionState) {
                    if (!checked) addItem(TagData(tag))
                    else removeItem(TagData(tag))
                }
            }
            Spacer(Modifier.height(Spacing.md))
            AnimatedButton(
                modifier = Modifier.fillMaxWidth().padding(Spacing.xs),
                onClick = {
                    if (state.groups.isNotEmpty() && state.userIdentifiers.isNotEmpty()) try {
                        scope.launch {
                            state.updateTags(tagSelectionState.selectedItems.map { it.name })
                            val message = state.toMessageData(user)
                            sendMessage(client = client, message = message, accessToken = accessToken)
                            state.clear()
                        }
                    } catch (e: Exception) {
                        error = e
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(end = Spacing.sm),
                )
                Text(
                    text = stringResource(Res.string.send),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }

    Scaffold(
        topBar = {
            Surface(color = MaterialTheme.colorScheme.surface) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.xl, vertical = Spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(Res.string.write_message),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
    Box(Modifier.fillMaxSize().padding(innerPadding)) {
        if (isExpanded) {
            // Side-by-side layout for wide screens
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                // Left pane — recipients
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.35f)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .padding(Spacing.lg)
                        .verticalScroll(rememberScrollState()),
                ) {
                    recipientsPanel()
                }

                VerticalDivider(modifier = Modifier.fillMaxHeight())

                // Right pane — compose
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.65f)
                        .padding(Spacing.lg),
                ) {
                    composePanel()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                recipientsPanel()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    composePanel()
                }
            }
        }

        // Overlay panels (selection dialogs)
        if (isGroupTabOpened && !isIndividualTabOpened && !isTagSelectTabOpened)
            GroupSelect(
                snapshot = groupSelectionState.snapshot() as GroupSelectionStateModel,
                onCancelClick = { isGroupTabOpened = false },
                onApplyClick = { groups -> state.updateGroups(groups.map { it.externalId }) },
            )

        if (isTagSelectTabOpened && !isGroupTabOpened && !isIndividualTabOpened)
            TagSelect(
                snapshot = tagSelectionState.snapshot() as TagSelectionStateModel,
                onCancelClick = { isTagSelectTabOpened = false },
                onApplyClick = { tags -> state.updateTags(tags.map { it.name }) },
            )

        if (isIndividualTabOpened && !isGroupTabOpened && !isTagSelectTabOpened)
            IndividualSelect(
                snapshot = userSelectionState.snapshot() as UserSelectionStateModel,
                onCancelClick = { isIndividualTabOpened = false },
                onApplyClick = { users -> state.updateUserIdentifiers(users.map { it.externalId }) },
            )

        if (error != null) ErrorDialog(error!!)
    }
    } // end Scaffold content
} // end WriteMessageScreen
