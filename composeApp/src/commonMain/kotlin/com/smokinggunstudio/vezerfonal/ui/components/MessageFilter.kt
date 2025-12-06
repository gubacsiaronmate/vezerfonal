package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.toLocalDateTime
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
@Preview
fun MessageFilter(
    state: MessageFilterState,
    tabOpenedClick: ClickEvent,
    modifier: Modifier = Modifier,
    scrollLockedBySliderCallback: CallbackEvent<Boolean>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            value = state.senderName,
            onValueChange = state::updateSenderName,
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
                valueRange = state.earliestMessageUnixTime.toFloat()..state.latestMessageUnixTime,
                onRangeSelected = { range ->
                    state.updateSelectedStartDate(range.start.toLong())
                    state.updateSelectedEndDate(range.endInclusive.toLong())
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) { scrollLockedBySliderCallback(it) }
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
                checked = state.isImportant,
                onCheckedChange = state::updateIsImportant,
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
                checked = state.isWaitingForAnswer,
                onCheckedChange = state::updateIsWaitingForAnswer,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = state::updateSearchQuery,
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
        HorizontallyScrollableTagSelect(
            state = state.tagSelectionState,
            tabOpenedCallback = tabOpenedClick
        )
    }
}
