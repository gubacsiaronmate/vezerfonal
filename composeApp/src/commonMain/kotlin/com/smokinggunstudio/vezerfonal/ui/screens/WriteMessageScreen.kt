package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.network.api.sendMessage
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
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
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun WriteMessageScreen(
    user: UserData,
    accessToken: String,
    guiao: List<GroupData>,
    userList: List<UserData>,
    tagList: List<TagData>,
    scrollLockedBySliderCallback: CallbackFunction<Boolean> = {},
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    val state = remember { WriteMessageStateController(WriteMessageStateModel()) }
    val snackbarHostState = remember { SnackbarHostState() }
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

    val onSend: () -> Unit = {
        if (state.groups.isNotEmpty() || state.userIdentifiers.isNotEmpty()) {
            scope.launch {
                try {
                    state.updateTags(tagSelectionState.selectedItems.map { it.name })
                    val message = state.toMessageData(user)
                    sendMessage(client = client, message = message, accessToken = accessToken)
                    state.clear()
                } catch (e: Exception) {
                    error = e
                }
            }
        }
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
    )
    val cardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    val cardShape = MaterialTheme.shapes.extraLarge

    @Composable
    fun RecipientsCard() {
        Card(
            shape = cardShape,
            colors = cardColors,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.lg),
        ) {
            SettingRow(
                imageVector = Icons.Filled.Groups,
                text = stringResource(Res.string.groups),
                trailing = @Composable {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (state.groups.isNotEmpty()) {
                            Badge { Text(state.groups.size.toString()) }
                            Spacer(Modifier.width(Spacing.sm))
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                onClick = { isGroupTabOpened = true },
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Spacing.lg),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            SettingRow(
                imageVector = Icons.Outlined.Person,
                text = stringResource(Res.string.individuals),
                trailing = @Composable {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (state.userIdentifiers.isNotEmpty()) {
                            Badge { Text(state.userIdentifiers.size.toString()) }
                            Spacer(Modifier.width(Spacing.sm))
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                onClick = { isIndividualTabOpened = true },
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Spacing.lg),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            SettingRow(
                imageVector = if (state.isUrgent) Icons.Filled.Error else Icons.Outlined.ErrorOutline,
                text = stringResource(Res.string.urgent),
                trailing = @Composable {
                    Switch(
                        checked = state.isUrgent,
                        onCheckedChange = { state.updateUrgency(it) },
                    )
                },
                onClick = { state.updateUrgency(!state.isUrgent) },
            )
        }
    }

    @Composable
    fun ComposeCard(
        modifier: Modifier = Modifier,
        messageModifier: Modifier = Modifier.heightIn(min = 180.dp),
    ) {
        Card(
            shape = cardShape,
            colors = cardColors,
            modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.lg),
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = state::updateTitle,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(Res.string.subject),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                singleLine = true,
                colors = fieldColors,
                textStyle = MaterialTheme.typography.titleMedium,
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Spacing.lg),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            OutlinedTextField(
                value = state.content,
                onValueChange = state::updateContent,
                modifier = Modifier.fillMaxWidth().then(messageModifier),
                placeholder = {
                    Text(
                        text = stringResource(Res.string.message),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                colors = fieldColors,
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Spacing.lg),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            ReactionBar(
                buttonEmojis = state.availableReactions,
                onSetEmoji = { i, emoji -> state.addReaction(emoji, i) },
                onClearEmoji = { i -> state.removeReaction(i) },
                showBackground = false,
                snackbarHostState = snackbarHostState,
            )
        }
    }

    @Composable
    fun BottomActions() {
        ComposeTagRow(
            selectedTags = tagSelectionState.selectedItems,
            onRemoveTag = { tagSelectionState.removeItem(it) },
            onAddTagsClick = { isTagSelectTabOpened = true },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        IconButton(onClick = onSend) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = stringResource(Res.string.send),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (isExpanded) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left pane — recipients
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.38f)
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = Spacing.lg),
                        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                    ) {
                        Text(
                            text = stringResource(Res.string.to),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = Spacing.xl),
                        )
                        RecipientsCard()
                    }
                    VerticalDivider(modifier = Modifier.fillMaxHeight())
                    // Right pane — compose
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.62f)
                            .padding(vertical = Spacing.lg),
                        verticalArrangement = Arrangement.spacedBy(Spacing.md),
                    ) {
                        ComposeCard(
                            modifier = Modifier.weight(1f),
                            messageModifier = Modifier.weight(1f),
                        )
                        BottomActions()
                        Spacer(Modifier.height(Spacing.md))
                    }
                }
            } else {
                // Mobile — single scrollable column
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md),
                ) {
                    Spacer(Modifier.height(Spacing.sm))
                    Text(
                        text = stringResource(Res.string.to),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = Spacing.xl),
                    )
                    RecipientsCard()
                    ComposeCard()
                    BottomActions()
                    Spacer(Modifier.height(Spacing.xl))
                }
            }

            // Overlay selection panels
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
                    onApplyClick = { tags ->
                        val newSet = tags.toSet()
                        (tagSelectionState.selectedItems - newSet).forEach { tagSelectionState.removeItem(it) }
                        (newSet - tagSelectionState.selectedItems).forEach { tagSelectionState.addItem(it) }
                        isTagSelectTabOpened = false
                    },
                )

            if (isIndividualTabOpened && !isGroupTabOpened && !isTagSelectTabOpened)
                IndividualSelect(
                    snapshot = userSelectionState.snapshot() as UserSelectionStateModel,
                    onCancelClick = { isIndividualTabOpened = false },
                    onApplyClick = { users -> state.updateUserIdentifiers(users.map { it.externalId }) },
                )

            if (error != null) ErrorDialog(error!!)
        }
    }
}
