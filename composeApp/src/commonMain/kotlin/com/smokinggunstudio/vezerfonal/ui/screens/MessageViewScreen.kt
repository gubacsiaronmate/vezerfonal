package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.now
import com.smokinggunstudio.vezerfonal.ui.components.DisabledBottomPanel
import com.smokinggunstudio.vezerfonal.ui.components.HorizontallyScrollableTagList
import com.smokinggunstudio.vezerfonal.ui.components.RecipientReactionBottomPanel
import com.smokinggunstudio.vezerfonal.ui.helpers.capitalize
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.status

@Composable
fun MessageViewScreen(
    message: MessageData
) {
    val scope = rememberCoroutineScope()
    var top by remember { mutableStateOf(80.dp) }
    val statusAsStr = (message.status ?: MessageStatus.received).toString().capitalize()
    val statusString = "${stringResource(Res.string.status)}: $statusAsStr"
    
    Column(
        modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .background(color = MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    maxLines = 1,
                    text = message.title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineLarge,
                )
                
                Icon(
                    imageVector =
                        if (message.isUrgent) Icons.Filled.Error
                        else Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(32.dp).fillMaxWidth(.5F),
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    maxLines = 1,
                    text = message.author.name,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = statusString,
                    maxLines = 1,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        
        HorizontallyScrollableTagList(message.tags)
        
        HorizontalDivider(Modifier.height(1.dp).fillMaxWidth().padding(8.dp))
        
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = message.content,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp).fillMaxHeight(),
                )
            }

            if (message.reactedWith != null)
                DisabledBottomPanel(message.reactedWith!!)
            else RecipientReactionBottomPanel(
                availableReactions = message.availableReactions,
                modifier = Modifier.padding(top = top).align(Alignment.BottomCenter),
                onIsReactionBarVisible = { top = if (!it) 80.dp else 4.dp }
            ) {
                scope.launch {
                
                }
            }
        }
    }
}

@Preview
@Composable
private fun Asd1() {
    MessageViewScreen(
        MessageData(
            title = "Test Title",
            content = "Test Content: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc faucibus rhoncus orci. Suspendisse potenti. Vestibulum bibendum arcu a mattis porta. Donec arcu diam, egestas eget risus vel, scelerisque venenatis augue. Vivamus aliquam urna neque, in laoreet eros feugiat in. Praesent faucibus risus sed purus finibus lacinia. Quisque vel felis eget mauris euismod condimentum id vel libero. Nunc pellentesque erat at malesuada efficitur. Nunc elit enim, commodo vel efficitur non, convallis eget est. Proin risus massa, convallis eu facilisis nec, interdum quis ante. Phasellus at elit a eros interdum facilisis. Cras ac suscipit nulla. Cras bibendum vestibulum elit vitae tristique. Sed cursus ullamcorper sem a egestas. Integer vitae blandit nunc. Aliquam auctor neque in congue egestas. Nullam eget finibus odio, ac luctus nunc. Curabitur vitae enim a enim elementum tristique sit amet sit amet ante. Maecenas sit amet libero sem.",
            author = UserData(
                registrationCode = null,
                email = "",
                password = null,
                name = "Test Author",
                identifier = "",
                isAnyAdmin = true,
                isSuperAdmin = false
            ),
            tags = (0..12).map { "Test Tag $it" },
            isUrgent = true,
            status = MessageStatus.received,
            userIdentifiers = null,
            availableReactions = listOf("👍", "❤️", "🔥", "👏", "😂", "😒", "😒", "😒"),
            groups = null,
            sentAt = LocalDateTime.now().toString(),
            reactedWith = null
        )
    )
}

@Preview
@Composable
private fun Asd2() {
    MessageViewScreen(
        MessageData(
            title = "Test Title",
            content = "Test Content: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc faucibus rhoncus orci. Suspendisse potenti. Vestibulum bibendum arcu a mattis porta. Donec arcu diam, egestas eget risus vel, scelerisque venenatis augue. Vivamus aliquam urna neque, in laoreet eros feugiat in. Praesent faucibus risus sed purus finibus lacinia. Quisque vel felis eget mauris euismod condimentum id vel libero. Nunc pellentesque erat at malesuada efficitur. Nunc elit enim, commodo vel efficitur non, convallis eget est. Proin risus massa, convallis eu facilisis nec, interdum quis ante. Phasellus at elit a eros interdum facilisis. Cras ac suscipit nulla. Cras bibendum vestibulum elit vitae tristique. Sed cursus ullamcorper sem a egestas. Integer vitae blandit nunc. Aliquam auctor neque in congue egestas. Nullam eget finibus odio, ac luctus nunc. Curabitur vitae enim a enim elementum tristique sit amet sit amet ante. Maecenas sit amet libero sem.",
            author = UserData(
                registrationCode = null,
                email = "",
                password = null,
                name = "Test Author",
                identifier = "",
                isAnyAdmin = true,
                isSuperAdmin = false
            ),
            tags = (0..12).map { "Test Tag $it" },
            isUrgent = false,
            status = MessageStatus.received,
            userIdentifiers = null,
            availableReactions = null,
            groups = null,
            sentAt = LocalDateTime.now().toString(),
            reactedWith = null
        )
    )
}