package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.asFormattedLDTStr
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing

@Composable
fun ListItem(
    title: String,
    author: String,
    onClick: Function,
    content: String = "",
    sentAt: Long? = null,
    isUrgent: Boolean = false,
    isRead: Boolean = true,
    status: MessageStatus? = null,
    tags: List<String> = emptyList(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg, vertical = Spacing.md),
            verticalAlignment = Alignment.Top,
        ) {
            // Avatar with optional urgency dot
            Box {
                ProfilePicture(name = author, size = 40.dp)
                if (isUrgent) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.TopEnd)
                            .background(MaterialTheme.colorScheme.error, CircleShape)
                    )
                }
            }

            Spacer(Modifier.width(Spacing.md))

            Column(modifier = Modifier.weight(1f)) {
                // Row 1: author name + timestamp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = author,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (!isRead) FontWeight.Bold else FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (sentAt != null) {
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = sentAt.asFormattedLDTStr,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Spacer(Modifier.height(2.dp))

                // Row 2: message title
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (!isRead) FontWeight.SemiBold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Row 3: content preview
                if (content.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                // Row 4: tag chips + status badge
                if (tags.isNotEmpty() || status != null) {
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        tags.take(3).forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(end = 4.dp),
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                )
                            }
                        }
                        Spacer(Modifier.weight(1f))
                        status?.let {
                            val label = when (it) {
                                MessageStatus.sent -> "✓ Sent"
                                MessageStatus.received -> "✓ Received"
                                MessageStatus.read -> "● Read"
                            }
                            val color = when (it) {
                                MessageStatus.sent -> MaterialTheme.colorScheme.tertiary
                                MessageStatus.received -> MaterialTheme.colorScheme.primary
                                MessageStatus.read -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = color,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }

            // Unread indicator dot
            if (!isRead) {
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.Top)
                        .padding(top = 4.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            }
        }

        // Indented divider (starts after the avatar column)
        HorizontalDivider(
            modifier = Modifier.padding(start = 68.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        )
    }
}
