package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
@Preview
fun MessageFilter(
    modifier: Modifier = Modifier
) {
    val earliest = 1731379200f
    val latest = 1731465600f
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(
                    text = stringResource(Res.string.senders_name),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Column {
            Text(
                stringResource(Res.string.time_sent),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            MessageTimeRangeSlider(
                valueRange = earliest..latest,
                initialRange = earliest..latest,
                formatLabel = { ts: Float ->
                    val instant = Instant.fromEpochSeconds(ts.toLong())
                    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                    "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
                },
                onRangeSelected = { range ->
                    val from = range.start.toLong()
                    val to = range.endInclusive.toLong()
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.important),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            Switch(
                checked = false,
                onCheckedChange = {},
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.waiting_for_answer),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            Switch(
                checked = false,
                onCheckedChange = {},
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            singleLine = true,
            label = {
                Text(
                    text = stringResource(Res.string.search_title_or_in_message_content),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        HorizontallyScrollableTagSelect()
    }
}
