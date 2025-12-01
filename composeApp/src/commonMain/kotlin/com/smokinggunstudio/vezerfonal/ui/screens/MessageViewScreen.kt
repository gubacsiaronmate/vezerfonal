package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.ui.components.HorizontallyScrollableTagList
import com.smokinggunstudio.vezerfonal.ui.components.RecipientReactionBar
import com.smokinggunstudio.vezerfonal.ui.components.RecipientReactionBottomPanel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun MessageViewScreen(
    messageData: MessageData = MessageData(
        title = "Test Title",
        content = "Test Content: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc faucibus rhoncus orci. Suspendisse potenti. Vestibulum bibendum arcu a mattis porta. Donec arcu diam, egestas eget risus vel, scelerisque venenatis augue. Vivamus aliquam urna neque, in laoreet eros feugiat in. Praesent faucibus risus sed purus finibus lacinia. Quisque vel felis eget mauris euismod condimentum id vel libero. Nunc pellentesque erat at malesuada efficitur. Nunc elit enim, commodo vel efficitur non, convallis eget est. Proin risus massa, convallis eu facilisis nec, interdum quis ante. Phasellus at elit a eros interdum facilisis. Cras ac suscipit nulla. Cras bibendum vestibulum elit vitae tristique. Sed cursus ullamcorper sem a egestas. Integer vitae blandit nunc. Aliquam auctor neque in congue egestas. Nullam eget finibus odio, ac luctus nunc. Curabitur vitae enim a enim elementum tristique sit amet sit amet ante. Maecenas sit amet libero sem.",
        author = "Test Author",
        tags = listOf("Test Tag"),
        isUrgent = false,
        status = null,
        userIdentifiers = null,
        availableReactions = null,
        groups = null
    )
) {
    Column(
        modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .background(color = MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = messageData.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge)
        }
        HorizontallyScrollableTagList(tags = messageData.tags)
        HorizontalDivider(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .padding(8.dp))
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())) {
            Text(
                text = messageData.content,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            RecipientReactionBar(
                reactions = listOf("👍", "❤️", "🔥", "👏", "😂", "😒"),
                onReactionSelected = {}
            )
            RecipientReactionBottomPanel()
        }
    }
}